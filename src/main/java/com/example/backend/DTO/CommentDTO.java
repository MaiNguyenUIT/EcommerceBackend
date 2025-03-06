package com.example.backend.DTO;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentDTO {
    private String description;
    private LocalDateTime updatedAt;
}
