package com.example.backend.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RatingDTO {
    private int star;
    private String review;
    private LocalDateTime reviewDate = LocalDateTime.now();
}
