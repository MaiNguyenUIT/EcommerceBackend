package com.example.backend.DTO.response;

import com.example.backend.ENUM.PRODUCT_STATE;
import lombok.Data;

import java.util.List;

@Data
public class ProductResponse {
    private String id;
    private String name;
    private String categoryName;
    private int quantity;
    private int regularPrice;
    private int price;
    private String description;
    private List<String> image;
    private PRODUCT_STATE productState;
}
