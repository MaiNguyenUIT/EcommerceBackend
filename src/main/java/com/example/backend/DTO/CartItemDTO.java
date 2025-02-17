package com.example.backend.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CartItemDTO {
    private String productId;
    private String productName;
    private LocalDateTime addedAt = LocalDateTime.now();
}
