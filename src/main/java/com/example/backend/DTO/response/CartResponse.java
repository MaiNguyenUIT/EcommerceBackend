package com.example.backend.DTO.response;

import com.example.backend.model.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartResponse {
    private String id;
    private int totalPrice;
    private List<CartItem> cartItems = new ArrayList<>();
    private int totalItem;
    private String userId;
}
