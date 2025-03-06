package com.example.backend.utils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse<T> {
    private T data;
    private int status;
    private String message;
}
