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
    @Autowired CheckToxicService checkToxicService;
    @Override
    public RatingResponse createRating(RatingDTO ratingDTO, String userId, String productId) {
        RatingResponse ratingResponse = new RatingResponse();
        Rating rating = RatingMapper.INSTANCE.toEntity(ratingDTO);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id" + userId));

        if(checkToxicService.checkToxic(ratingDTO.getReview())){
            throw new BadRequestException("Rating contains toxic content and cannot be submitted.");
        } else {
            rating.setUserId(userId);
            rating.setProductId(productId);
            ratingRepository.save(rating);
            //tra ve response
            ratingResponse.setReview(rating.getReview());
            ratingResponse.setUsername(userRepository.findById(userId).orElse(null).getName());
            ratingResponse.setStar(rating.getStar());
            ratingResponse.setReviewDate(rating.getReviewDate());
        }
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
