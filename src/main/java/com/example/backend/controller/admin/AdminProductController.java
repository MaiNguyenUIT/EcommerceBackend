package com.example.backend.controller.admin;

import com.example.backend.ApiResult.ApiResult;
import com.example.backend.DTO.ProductDTO;
import com.example.backend.model.Product;
import com.example.backend.model.User;
import com.example.backend.service.ProductService;
import com.example.backend.service.UserService;
import com.example.backend.utils.MapResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ApiResult<ProductDTO>> createProduct(@RequestHeader("Authorization") String jwt, @RequestBody ProductDTO productDTO) throws Exception{
        User user = userService.findUserByJwtToken(jwt);
        ProductDTO newProduct = productService.createProduct(productDTO);
        ApiResult<ProductDTO> apiResult = mapResult.map(newProduct, "Create product success");
        return new ResponseEntity<>(apiResult, HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<ProductDTO>> updateProduct(@RequestHeader("Authorization") String jwt, @RequestBody ProductDTO productDTO, @PathVariable String id) throws Exception{
        User user = userService.findUserByJwtToken(jwt);
        ProductDTO updateProduct = productService.updateProduct(productDTO, id);
        ApiResult<ProductDTO> apiResult = mapResult.map(updateProduct, "Update product success");
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@RequestHeader("Authorization") String jwt, @PathVariable String id) throws Exception{
        User user = userService.findUserByJwtToken(jwt);
        productService.deleteProduct(id);
        return new ResponseEntity<>("Delete product successfully", HttpStatus.OK);
    }
}
