package com.example.backend.repository;

import com.example.backend.model.RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {
    Optional<RefreshToken> findBytoken(String token);
    Optional<RefreshToken> findByuserId(String userId);
    void deleteByUserId(String userId);
}
