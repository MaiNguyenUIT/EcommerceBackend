package com.example.backend.service;

import com.example.backend.DTO.UserAccountDTO;
import com.example.backend.DTO.request.LoginRequest;
import com.example.backend.DTO.request.RefreshTokenRequest;
import com.example.backend.DTO.respone.AuthRespone;
import com.example.backend.DTO.respone.TokenRespone;

public interface AuthService {
    AuthRespone signIn(LoginRequest loginRequest);
    AuthRespone signUp(UserAccountDTO userAccountDTO);
    String logOut(String jwt) throws Exception;
    TokenRespone refreshToken(RefreshTokenRequest refreshTokenRequest);

}
