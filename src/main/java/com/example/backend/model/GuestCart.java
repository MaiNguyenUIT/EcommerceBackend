package com.example.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@RedisHash(value = "GuestCart", timeToLive = 86400) // Giữ 24h
@Data
public class GuestCart implements Serializable {
    @Id
    private String sessionId;
    private int totalPrice;
    private List<CartItem> cartItems = new ArrayList<>();
    private int totalItem;
}
