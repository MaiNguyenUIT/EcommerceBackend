package com.example.backend.service;

import com.example.backend.DTO.WishListItemDTO;
import com.example.backend.model.GuestWishList;
import com.example.backend.model.WishList;

public interface GuestWishListService {
    GuestWishList addToWishList(String sessionId, WishListItemDTO wishListItemDTO);
    GuestWishList removeFromWishList(String sessionId, String productId);
    GuestWishList getWishList(String sessionId);
    GuestWishList addWishListItemToCart(String sessionId, String productId);
    void clearWishList(String sessionId);
}
