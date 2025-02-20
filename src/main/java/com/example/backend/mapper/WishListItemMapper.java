package com.example.backend.mapper;

import com.example.backend.DTO.WishListItemDTO;
import com.example.backend.model.WishList;
import com.example.backend.model.WishListItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WishListItemMapper {
    WishListItemMapper INSTANCE = Mappers.getMapper(WishListItemMapper.class);
    WishListItem toEntity(WishListItemDTO dto);

    WishListItemDTO toDTO(WishListItem wishListItem);
}
