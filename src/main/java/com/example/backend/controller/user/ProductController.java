package com.example.backend.controller.user;

import com.example.backend.ApiResult.ApiResult;
import com.example.backend.DTO.ProductDTO;
import com.example.backend.model.Product;
import com.example.backend.service.ProductService;
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
    public ResponseEntity<ApiResult<Product>> getProductById(@PathVariable String id){
        Product product = productService.getProduct(id);
        ApiResult apiResult = mapResult.map(product, "Get product successfully");
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }
    @GetMapping()
    public ResponseEntity<ApiResult<List<Product>>> getAllProduct(){
        List<Product> products = productService.getAllActiveProduct();
        ApiResult apiResult = mapResult.map(products, "Get all product successfully");
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }
    @GetMapping("/category/{categoryName}")
    public ResponseEntity<ApiResult<List<Product>>> getProductByCategory(@PathVariable String categoryName){
        List<Product> products = productService.filterProductByCategory(categoryName);
        ApiResult apiResult = mapResult.map(products, "Get product by category " + categoryName + " successfully");
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }
}
