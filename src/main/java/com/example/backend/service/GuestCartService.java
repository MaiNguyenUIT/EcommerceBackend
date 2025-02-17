package com.example.backend.service;

import com.example.backend.DTO.CartItemDTO;
import com.example.backend.model.Cart;
import com.example.backend.model.GuestCart;

public interface GuestCartService {
    GuestCart addItemToGuestCart(String sessionId, CartItemDTO cartItemDTO);
    GuestCart getCartBySession(String sessionId); // Lấy giỏ hàng của khách vãng lai
    GuestCart updateItemQuantity(String sessionId, String productId, int quantity); // Tăng, giảm, hoặc xóa
    GuestCart deleteItemFromGuestCart(String sessionId, String productId);
    void clearCart(String sessionId); // Xóa toàn bộ giỏ hàng
    GuestCart increaseCartItem(String sessionId, String productId);
    GuestCart decreaseCartItem(String sessionId, String productId);
}
