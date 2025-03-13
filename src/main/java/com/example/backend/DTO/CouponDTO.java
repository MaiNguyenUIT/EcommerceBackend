package com.example.backend.DTO;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class CouponDTO {
    private String name;
    private int discount;
    private int quantity;
    private LocalDateTime expiry;
}
