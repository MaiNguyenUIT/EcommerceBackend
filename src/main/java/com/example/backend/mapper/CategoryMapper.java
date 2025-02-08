package com.example.backend.mapper;

import com.example.backend.DTO.CategoryDTO;
import com.example.backend.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);
    @Mapping(target = "createdAt", ignore = true)  // Bỏ qua createdAt khi map từ DTO
    @Mapping(target = "updatedAt", ignore = true)  // Bỏ qua updatedAt khi map từ DTO
    Category toEntity(CategoryDTO dto);

    CategoryDTO toDTO(Category category);
}
