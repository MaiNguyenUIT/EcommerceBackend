package com.example.backend.serviceImpl;

import com.example.backend.exception.BadRequestException;
import com.example.backend.exception.NotFoundException;
import com.example.backend.model.RefreshToken;
import com.example.backend.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    public RefreshToken createRefreshToken(String userId) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserId(userId);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusSeconds(7 * 24 * 60 * 60)); // 7 ngày

        return refreshTokenRepository.save(refreshToken);
    }
    public String getRefreshTokenByUserId(String userId){
        RefreshToken refreshToken = refreshTokenRepository.findByuserId(userId)
                .orElseThrow(() -> new BadRequestException("Refresh token is expiry"));
        return refreshToken.getToken();
    }
}
