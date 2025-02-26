package com.example.backend.DTO;

import com.example.backend.ENUM.PRODUCT_STATE;
import lombok.Data;
import lombok.Getter;

import java.util.List;
@Data
public class ProductDTO {
    private String name;
    private String categoryName;
    private int quantity;
    private int regularPrice;
    private int price;
    private String description;
    private List<String> image;
    private PRODUCT_STATE productState;
}
