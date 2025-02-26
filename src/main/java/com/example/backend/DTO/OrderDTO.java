package com.example.backend.DTO;

import com.example.backend.ENUM.ORDER_STATUS;
import com.example.backend.ENUM.PAYMENT_METHOD;
import com.example.backend.model.OrderItem;
import com.example.backend.model.ShippingAddress;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private LocalDateTime orderDateTime = LocalDateTime.now();
    private String coupon;
    private ShippingAddress shippingAddress;
    private PAYMENT_METHOD paymentMethod;
}
