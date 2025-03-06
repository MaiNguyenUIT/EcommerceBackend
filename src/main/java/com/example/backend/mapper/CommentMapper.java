package com.example.backend.mapper;

import com.example.backend.DTO.CategoryDTO;
import com.example.backend.DTO.CommentDTO;
import com.example.backend.model.Category;
import com.example.backend.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
@Mapper
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Comment toEntity(CommentDTO dto);

    CommentDTO toDTO(Comment comment);
}
