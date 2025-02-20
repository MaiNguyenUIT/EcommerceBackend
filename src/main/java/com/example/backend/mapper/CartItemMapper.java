package com.example.backend.mapper;

import com.example.backend.DTO.CartItemDTO;
import com.example.backend.DTO.CategoryDTO;
import com.example.backend.model.CartItem;
import com.example.backend.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CartItemMapper {
    CartItemMapper INSTANCE = Mappers.getMapper(CartItemMapper.class);
    @Mapping(target = "quantity", ignore = true)
    CartItem toEntity(CartItemDTO dto);

    CartItemDTO toDTO(CartItem cartItem);
}
