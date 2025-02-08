package com.example.backend.serviceImpl;

import com.example.backend.DTO.CategoryDTO;
import com.example.backend.exception.BadRequestException;
import com.example.backend.exception.NotFoundException;
import com.example.backend.mapper.CategoryMapper;
import com.example.backend.model.Category;
import com.example.backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Service
public class CategoryService implements com.example.backend.service.CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) throws Exception {
        if(categoryRepository.findByTitle(categoryDTO.getTitle()) != null){
            throw new BadRequestException("Category is already exist");
        }
        Category category = CategoryMapper.INSTANCE.toEntity(categoryDTO);
        category.setCreatedAt(LocalDateTime.now());
        return CategoryMapper.INSTANCE.toDTO(categoryRepository.save(category));
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public CategoryDTO getCategoryById(String id) {
        return CategoryMapper.INSTANCE.toDTO(categoryRepository.findById(id).
                orElseThrow(() -> new NotFoundException("Category not found with ID: " + id)));
    }

    @Override
    public void deleteCategoryById(String id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, String id) throws Exception {
        Category category = categoryRepository.findById(id).orElse(null);
        if(category == null){
            throw new NotFoundException("Category not found");
        }
        if(categoryRepository.findByTitle(categoryDTO.getTitle()) != null){
            throw new BadRequestException("Category is already exist");
        }
        category.setTitle(categoryDTO.getTitle());
        category.setUpdatedAt(LocalDateTime.now());
        return CategoryMapper.INSTANCE.toDTO(categoryRepository.save(category));
    }
}
