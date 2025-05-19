package com.example.backend.mapper.ResponseMapper;

import com.example.backend.DTO.response.CartResponse;
import com.example.backend.model.Cart;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

public interface CartResMapper {
    CartResMapper INSTANCE = Mappers.getMapper(CartResMapper.class);
    @Mapping(target = "cartItems", ignore = true)
    CartResponse toRes(Cart cart);
}
