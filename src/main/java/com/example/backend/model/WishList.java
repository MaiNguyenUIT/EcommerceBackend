package com.example.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(
        "wishlist"
)
public class WishList {
    @Id
    private String id;
    private List<WishListItem> wishListItems = new ArrayList<>();
    private String userId;
}
