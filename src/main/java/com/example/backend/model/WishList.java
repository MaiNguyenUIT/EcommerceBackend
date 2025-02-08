package com.example.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(
        "wishlist"
)
public class WishList {
    @Id
    private String id;
    private String productId;
    private String productName;
}
