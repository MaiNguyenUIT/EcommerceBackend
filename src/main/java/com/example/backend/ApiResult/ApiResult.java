package com.example.backend.ApiResult;

import lombok.Data;
@Data
public class ApiResult<T> {
    private T data;
    private String message;
}
