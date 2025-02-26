package com.example.backend.mapper;

import com.example.backend.DTO.CategoryDTO;
import com.example.backend.DTO.UserAccountDTO;
import com.example.backend.model.Category;
import com.example.backend.model.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Mapper
public interface UserAccountMapper {
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    UserAccountMapper INSTANCE = Mappers.getMapper(UserAccountMapper.class);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "photo", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "password", ignore = true) // Sẽ xử lý trong @AfterMapping

    User toEntity(UserAccountDTO dto);
    UserAccountDTO toDTO(User user);
    @AfterMapping
    default void encodePassword(@MappingTarget User user, UserAccountDTO dto) {
        if (dto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
    }
}
