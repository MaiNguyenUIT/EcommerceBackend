package com.example.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@RedisHash(value = "GuestWishList", timeToLive = 86400)
public class GuestWishList implements Serializable {
    @Id
    private String sessionId;
    private List<WishListItem> wishListItems = new ArrayList<>();
}
