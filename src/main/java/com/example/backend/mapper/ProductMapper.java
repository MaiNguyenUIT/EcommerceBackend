package com.example.backend.mapper;

import com.example.backend.DTO.ProductDTO;
import com.example.backend.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);
    @Mapping(target = "id", ignore = true) // ID sẽ do database sinh ra
    @Mapping(target = "sold", ignore = true) // Nếu DTO không có thuộc tính này
    @Mapping(target = "rating", ignore = true) // Nếu không cần đánh giá
    @Mapping(target = "productState", ignore = true)
    Product toEntity(ProductDTO productDTO);


    ProductDTO toDTO(Product product);
}
