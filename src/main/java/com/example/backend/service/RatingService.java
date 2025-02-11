package com.example.backend.service;

import com.example.backend.DTO.RatingDTO;
import com.example.backend.DTO.response.RatingResponse;
import com.example.backend.model.Rating;

import java.util.List;

public interface RatingService {
    RatingResponse createRating(RatingDTO ratingDTO, String userId, String productId);
    RatingResponse updateRating(RatingDTO ratingDTO, String userId, String productId);
    void deleteRating(String ratingId);
    List<Rating> getAllRatingByProductId(String productId);
}
