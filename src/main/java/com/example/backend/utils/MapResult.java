package com.example.backend.utils;

import com.example.backend.ApiResult.ApiResult;
import org.springframework.stereotype.Component;

@Component
public class MapResult<T> {
    public ApiResult<T> map(T data, String message){
        ApiResult<T> apiResult = new ApiResult<>();
        apiResult.setData(data);
        apiResult.setMessage(message);
        return apiResult;
    }
}
