package com.example.backend.DTO.respone;

import lombok.Data;

@Data
public class TokenRespone {
    private String refreshToken;
    private String accessToken;
}
