package com.example.backend.mapper;

import com.example.backend.DTO.ProductDTO;
import com.example.backend.DTO.RatingDTO;
import com.example.backend.model.Product;
import com.example.backend.model.Rating;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RatingMapper {
    RatingMapper INSTANCE = Mappers.getMapper(RatingMapper.class);
    @Mapping(target = "id", ignore = true) // ID sẽ do database sinh ra
    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "userId", ignore = true)
    Rating toEntity(RatingDTO ratingDTO);

    ProductDTO toDTO(Rating rating);
}
