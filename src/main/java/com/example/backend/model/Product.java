package com.example.backend.model;

import com.example.backend.ENUM.PRODUCT_STATE;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(
        "products"
)
public class Product {
    @Id
    private String id;
    private String name;
    private String categoryName;
    private int quantity;
    private int sold = 0;
    private int regularPrice;
    private int price;
    private String description;
    private List<String> image;
    private PRODUCT_STATE productState;
}
