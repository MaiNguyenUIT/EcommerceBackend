package com.example.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(
        "carts"
)
public class Cart {
    @Id
    private String id;
    private String totalPrice;
    private List<CartItem> cartItems;
    private int totalItem;
}
