package com.example.backend.DTO.respone;

import com.example.backend.ENUM.USER_ROLE;
import lombok.Data;

@Data
public class AuthRespone {
    private String refreshToken;
    private String accessToken;
    private String message;
    private USER_ROLE role;
}