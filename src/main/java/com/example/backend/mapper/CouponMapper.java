package com.example.backend.mapper;

import com.example.backend.DTO.CategoryDTO;
import com.example.backend.DTO.CouponDTO;
import com.example.backend.model.Category;
import com.example.backend.model.Coupon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
@Mapper
public interface CouponMapper {
    CouponMapper INSTANCE = Mappers.getMapper(CouponMapper.class);
    @Mapping(target = "id", ignore = true)  // Bỏ qua id khi map từ DTO
    Coupon toEntity(CouponDTO dto);
    @Mappings({
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "discount", target = "discount"),
            @Mapping(source = "quantity", target = "quantity"),
            @Mapping(source = "expiry", target = "expiry", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    })
    CouponDTO toDTO(Coupon coupon);
}
