package com.example.backend.serviceImpl;

import com.example.backend.DTO.RatingDTO;
import com.example.backend.DTO.response.RatingResponse;
import com.example.backend.exception.BadRequestException;
import com.example.backend.exception.NotFoundException;
import com.example.backend.mapper.RatingMapper;
import com.example.backend.model.Rating;
import com.example.backend.model.User;
import com.example.backend.repository.RatingRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RatingService implements com.example.backend.service.RatingService {
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public RatingResponse createRating(RatingDTO ratingDTO, String userId, String productId) {
        RatingResponse ratingResponse = new RatingResponse();
        Rating rating = RatingMapper.INSTANCE.toEntity(ratingDTO);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id" + userId));

        String flaskApiUrl = "http://localhost:5000/predict"; // Địa chỉ API Flask
        RestTemplate restTemplate = new RestTemplate();

        // Tạo nội dung request
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("review", rating.getReview());
        HttpHeaders headers = restTemplate.headForHeaders(flaskApiUrl);
        headers.setContentType(MediaType.APPLICATION_JSON);
        org.springframework.http.HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        // Gửi yêu cầu POST đến Flask API
        ResponseEntity<Map> response = restTemplate.exchange(flaskApiUrl, HttpMethod.POST, (org.springframework.http.HttpEntity<?>) entity, Map.class);

        // Xử lý phản hồi từ API
        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && "Toxic".equals(responseBody.get("Toxic"))) {
                throw new BadRequestException("Review contains toxic content and cannot be submitted.");
            }
        } else {
            throw new RuntimeException("Error occurred while calling toxicity API: " + response.getStatusCode());
        }
        //Luu rating vao database
        rating.setUserId(userId);
        rating.setProductId(productId);
        rating.setName(user.getName());
        ratingRepository.save(rating);
        //tra ve response
        ratingResponse.setReview(rating.getReview());
        ratingResponse.setUsername(rating.getName());
        ratingResponse.setStar(rating.getStar());
        ratingResponse.setReviewDate(rating.getReviewDate());
        return ratingResponse;
    }

    @Override
    public RatingResponse updateRating(RatingDTO ratingDTO, String userId, String productId) {
        return null;
    }

    @Override
    public void deleteRating(String ratingId) {
        ratingRepository.deleteById(ratingId);
    }

    @Override
    public List<Rating> getAllRatingByProductId(String productId) {
        return ratingRepository.findRatingsByProductIdSorted(productId, Sort.by(Sort.Direction.DESC, "reviewDate"));
    }
}
