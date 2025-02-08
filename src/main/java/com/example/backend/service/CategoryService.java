package com.example.backend.service;

import com.example.backend.DTO.CategoryDTO;
import com.example.backend.model.Category;

import java.util.List;

public interface CategoryService {
    CategoryDTO createCategory(CategoryDTO categoryDTO) throws Exception;
    List<Category> getAllCategory();
    CategoryDTO getCategoryById(String id);
    void deleteCategoryById(String id);
    CategoryDTO updateCategory(CategoryDTO categoryDTO, String id) throws Exception;
}
