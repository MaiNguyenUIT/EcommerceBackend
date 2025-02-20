package com.example.backend.repository;

import com.example.backend.model.WishList;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WishListRepository extends MongoRepository<WishList, String> {
    WishList findByuserId(String userId);
    void deleteByuserId(String userId);
}
