package com.example.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.ArrayList;
import java.util.List;

@Data
@RedisHash(value = "ClickItem", timeToLive = 86400)
public class ClickItem {
    @Id
    private String identified;
    private List<String> productId = new ArrayList<>();
}
