package com.example.backend.serviceImpl;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class TokenBlacklistService {

    private final StringRedisTemplate redisTemplate;
    private static final String BLACKLIST_PREFIX = "blacklist:";

    public TokenBlacklistService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Blacklist an access token in Redis with expiration time.
     */
    public void blacklistToken(String token, long expirationInMillis) {
        long expirationInSeconds = expirationInMillis / 1000;
        redisTemplate.opsForValue().set(BLACKLIST_PREFIX + token, "blacklisted", Duration.ofSeconds(expirationInSeconds));
    }

    /**
     * Check if an access token is blacklisted.
     */
    public boolean isTokenBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + token));
    }
}
