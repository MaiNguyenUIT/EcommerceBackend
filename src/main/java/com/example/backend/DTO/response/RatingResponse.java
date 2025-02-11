package com.example.backend.DTO.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RatingResponse {
    private int star;
    private String review;
    private LocalDateTime reviewDate;
    private String username;
}
