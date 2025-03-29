package com.example.backend.model;

import lombok.Data;

@Data
public class ProductStatistic {
    private String productId;
    private int quantity;

    public ProductStatistic(String productId, Integer quantitySold) {
        this.productId = productId;
        this.quantity = quantitySold;
    }
}
