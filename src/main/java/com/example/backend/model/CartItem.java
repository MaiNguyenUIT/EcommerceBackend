package com.example.backend.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CartItem {
    private String id;
    private String productName;
    private String productId;
    private int quantity;
    private LocalDateTime addedAt;
}
