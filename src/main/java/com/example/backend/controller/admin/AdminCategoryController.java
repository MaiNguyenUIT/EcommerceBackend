package com.example.backend.controller.admin;

import com.example.backend.ApiResult.ApiResult;
import com.example.backend.DTO.CategoryDTO;
import com.example.backend.model.User;
import com.example.backend.service.CategoryService;
import com.example.backend.service.UserService;
import com.example.backend.utils.MapResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin/category")
public class AdminCategoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;
    @Autowired
    private MapResult mapResult;
    @PostMapping("")
    public ResponseEntity<ApiResult<CategoryDTO>> createCategory(@RequestBody CategoryDTO categoryDTO) throws Exception{
        CategoryDTO newCategory = categoryService.createCategory(categoryDTO);
        ApiResult<CategoryDTO> apiResult = mapResult.map(newCategory, "Create category successfully");
        return new ResponseEntity<>(apiResult, HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<CategoryDTO>> updateCategory(@RequestBody CategoryDTO categoryDTO, @PathVariable String id) throws Exception{
        CategoryDTO category = categoryService.updateCategory(categoryDTO, id);
        ApiResult<CategoryDTO> apiResult = mapResult.map(category, "Update category successfully");

        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult> deleteCategory(@PathVariable String id) throws Exception{
        categoryService.deleteCategoryById(id);
        ApiResult apiResult = mapResult.map(null,"Delete category successfully");
        return new ResponseEntity<>(apiResult, HttpStatus.OK);
    }
}
