package com.example.backend.serviceImpl;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class SecretKeyService {
    private final Environment environment;

    public SecretKeyService(Environment environment) {
        this.environment = environment;
    }

    public String getSecretKey() {
        return environment.getProperty("SECRET_KEY");
    }
}

