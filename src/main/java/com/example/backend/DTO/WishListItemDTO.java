package com.example.backend.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WishListItemDTO {
    private String productId;
    private String productName;
}
