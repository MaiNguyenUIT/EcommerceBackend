package com.example.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtConstant {
    public static String SECRET_KEY;

    @Value("${jwt.secretKey}")
    public void setSecretKey(String secretKey) {
        JwtConstant.SECRET_KEY = secretKey;
    }
    public static final String JWT_HEADER="Authorization";
}
