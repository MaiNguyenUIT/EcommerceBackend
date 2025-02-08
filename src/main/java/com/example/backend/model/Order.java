package com.example.backend.model;

import com.example.backend.ENUM.ORDER_STATUS;
import com.example.backend.ENUM.PAYMENT_METHOD;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(
        "orders"
)
@Data
public class Order {
    @Id
    private String id;
    private String userId;
    private LocalDateTime orderDateTime;
    private int orderAmount;
    private ORDER_STATUS orderStatus = ORDER_STATUS.PENDING;
    private String coupon;
    private ShippingAddress shippingAddress;
    private PAYMENT_METHOD paymentMethod;
    private Cart cart;
}
