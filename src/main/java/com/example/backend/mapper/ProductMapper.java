package com.example.backend.mapper;

import com.example.backend.DTO.ProductDTO;
import com.example.backend.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sold", ignore = true)
    Product toEntity(ProductDTO productDTO);


    ProductDTO toDTO(Product product);
}
