package com.example.backend.controller.user;

import com.example.backend.ApiResult.ApiResult;
import com.example.backend.DTO.CategoryDTO;
import com.example.backend.model.Category;
import com.example.backend.service.CategoryService;
import com.example.backend.service.UserService;
import com.example.backend.utils.MapResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private MapResult mapResult;
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<CategoryDTO>> getCategory(@PathVariable String id){
        CategoryDTO category = categoryService.getCategoryById(id);
        ApiResult<CategoryDTO> apiResult = mapResult.map(category, "Get category successfully");
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }
    @GetMapping("")
    public ResponseEntity<ApiResult<List<Category>>> getAllCategory(){
        List<Category> categories = categoryService.getAllCategory();
        ApiResult<List<Category>> apiResult = mapResult.map(categories, "Get all category successfully");
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }
}
