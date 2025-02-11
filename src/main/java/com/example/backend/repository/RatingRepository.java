package com.example.backend.repository;

import com.example.backend.model.Rating;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface RatingRepository extends MongoRepository<Rating, String> {
    @Query("{ 'productId': ?0 }")
    List<Rating> findRatingsByProductIdSorted(String productId, Sort sort);
}
