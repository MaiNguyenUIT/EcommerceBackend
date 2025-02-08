package com.example.backend.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(
        "ratings"
)
public class Rating {
    private int star;
    private String review;
    private LocalDateTime reviewDate;
    private String name;
    private String userId;
}
