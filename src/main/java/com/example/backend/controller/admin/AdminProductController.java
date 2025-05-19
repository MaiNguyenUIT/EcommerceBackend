package com.example.backend.controller.admin;

import com.example.backend.ApiResult.ApiResult;
import com.example.backend.DTO.CommentDTO;
import com.example.backend.DTO.ProductDTO;
import com.example.backend.DTO.response.ProductResponse;
import com.example.backend.model.Product;
import com.example.backend.model.User;
import com.example.backend.service.ProductService;
import com.example.backend.service.UserService;
import com.example.backend.utils.ApiResponse;
import com.example.backend.utils.MapResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin/product")
public class AdminProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private MapResult mapResult;
    @PostMapping
    public ResponseEntity<ApiResult<ProductDTO>> createProduct(@RequestBody ProductDTO productDTO) throws Exception{

        ProductDTO newProduct = productService.createProduct(productDTO);
        ApiResult<ProductDTO> apiResult = mapResult.map(newProduct, "Create product success");
        return new ResponseEntity<>(apiResult, HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<ProductDTO>> updateProduct(@RequestBody ProductDTO productDTO, @PathVariable String id) throws Exception{

        ProductDTO updateProduct = productService.updateProduct(productDTO, id);
        ApiResult<ProductDTO> apiResult = mapResult.map(updateProduct, "Update product success");
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable String id) throws Exception{
        productService.deleteProduct(id);
        return new ResponseEntity<>("Delete product successfully", HttpStatus.OK);
    }
    @GetMapping()
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProduct(){
        List<ProductResponse> products = productService.getAllProduct();
        ApiResponse<List<ProductResponse>> apiResponse = ApiResponse.<List<ProductResponse>>builder()
                .status(200)
                .data(products)
                .message("Get all product successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
