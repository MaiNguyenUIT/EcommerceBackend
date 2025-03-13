package com.example.backend.controller.user;

import com.example.backend.ApiResult.ApiResult;
import com.example.backend.DTO.ProductDTO;
import com.example.backend.DTO.response.ProductResponse;
import com.example.backend.model.Product;
import com.example.backend.service.ProductService;
import com.example.backend.utils.ApiResponse;
import com.example.backend.utils.MapResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private MapResult mapResult;
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable String id){
        ProductResponse product = productService.getProduct(id);
        ApiResponse<ProductResponse> apiResponse = ApiResponse.<ProductResponse>builder()
                .status(200)
                .data(product)
                .message("Get product successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }
    @GetMapping()
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProduct(){
        List<ProductResponse> products = productService.getAllActiveProduct();
        ApiResponse<List<ProductResponse>> apiResponse = ApiResponse.<List<ProductResponse>>builder()
                .status(200)
                .data(products)
                .message("Get all product successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductByCategory(@PathVariable String categoryId){
        List<ProductResponse> products = productService.filterProductByCategory(categoryId);
        ApiResponse<List<ProductResponse>> apiResponse = ApiResponse.<List<ProductResponse>>builder()
                .status(200)
                .data(products)
                .message("Get product by category successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
