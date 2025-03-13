package com.example.backend.service;

import com.example.backend.DTO.ProductDTO;
import com.example.backend.DTO.response.ProductResponse;
import com.example.backend.model.Product;

import java.util.List;

public interface ProductService {
    ProductDTO createProduct(ProductDTO productDTO);
    ProductDTO updateProduct(ProductDTO productDTO, String id);
    void deleteProduct(String id);
    ProductResponse getProduct(String id);
    List<ProductResponse> getAllProduct();
    List<ProductResponse> filterProductByCategory(String categoryName);
    List<ProductResponse> getAllActiveProduct();
}
