package com.example.backend.service;

import com.example.backend.DTO.WishListItemDTO;
import com.example.backend.model.WishList;

public interface WishListService {
    WishList addToWishList(String userId, WishListItemDTO wishListItemDTO);
    WishList removeFromWishList(String userId, String productId);
    WishList getUserWishList(String userId);
    WishList addWishListItemToCart(String userId, String productId);
    void clearWishList(String userId);
    WishList mergeGuestToUser(String sessionId, String userId);
}
