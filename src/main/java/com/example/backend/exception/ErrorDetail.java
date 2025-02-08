package com.example.backend.exception;

import lombok.Data;

@Data
public class ErrorDetail {
    private String code;
    private String message;
}
