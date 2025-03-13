package com.example.backend.mapper.ResponseMapper;

import com.example.backend.DTO.response.ProductResponse;
import com.example.backend.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductResMapper {
    ProductResMapper INSTANCE = Mappers.getMapper(ProductResMapper.class);
    @Mapping(target = "categoryName", ignore = true )
    ProductResponse toRes(Product product);
}
