package com.example.backend.service;

import com.example.backend.DTO.UserAccountDTO;
import com.example.backend.DTO.request.LoginRequest;
import com.example.backend.DTO.request.RefreshTokenRequest;
import com.example.backend.DTO.response.AuthResponse;
import com.example.backend.DTO.response.TokenResponse;

public interface AuthService {
    AuthResponse signIn(LoginRequest loginRequest);
    AuthResponse signUp(UserAccountDTO userAccountDTO);
    String logOut(String jwt) throws Exception;
    TokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

}
