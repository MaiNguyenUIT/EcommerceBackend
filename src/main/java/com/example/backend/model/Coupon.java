package com.example.backend.model;

import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(
        "coupons"
)
@Data
public class Coupon {
    @Id
    private String id;
    private String name;
    private int discount;
    private int quantity;
    private LocalDateTime expiry;
}
