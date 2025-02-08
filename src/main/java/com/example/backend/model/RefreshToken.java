package com.example.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document(
        "RefreshTokens"
)
public class RefreshToken {
    @Id
    private String id;
    private String token;
    private String userId; // Lưu ID user thay vì object User
    private Instant expiryDate;
}
