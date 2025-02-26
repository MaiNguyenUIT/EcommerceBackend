package com.example.backend.mapper;

import com.example.backend.DTO.CartItemDTO;
import com.example.backend.DTO.OrderDTO;
import com.example.backend.model.CartItem;
import com.example.backend.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);
    @Mapping(target = "orderStatus", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "orderAmount", ignore = true)
    Order toEntity(OrderDTO dto);

    OrderDTO toDTO(Order order);
}
