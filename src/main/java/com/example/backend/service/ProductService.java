package com.example.backend.service;

import com.example.backend.DTO.ProductDTO;
import com.example.backend.model.Product;

import java.util.List;

public interface ProductService {
    ProductDTO createProduct(ProductDTO productDTO);
    ProductDTO updateProduct(ProductDTO productDTO, String id);
    void deleteProduct(String id);
    Product getProduct(String id);
    List<Product> getAllProduct();
    List<Product> filterProductByCategory(String categoryName);
}
