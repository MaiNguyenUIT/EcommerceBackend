package com.example.backend.DTO.response;

import lombok.Data;

@Data
public class TokenResponse {
    private String refreshToken;
    private String accessToken;
}
