package com.example.backend.model;

import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(
        "categories"
)
@Data
public class Category {
    @Id
    private String id;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
