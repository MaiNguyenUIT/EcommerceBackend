package com.example.backend.model;

import lombok.Data;

@Data
public class OrderItem {
    private String productId;
    private int quantity;
}
