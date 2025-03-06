package com.example.backend.serviceImpl;

import com.example.backend.exception.BadRequestException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class CheckToxicService {
    public Boolean checkToxic(String description){
        String flaskApiUrl = "http://localhost:5000/predict"; // Địa chỉ API Flask
        RestTemplate restTemplate = new RestTemplate();

        // Tạo nội dung request
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("review", description);
        HttpHeaders headers = restTemplate.headForHeaders(flaskApiUrl);
        headers.setContentType(MediaType.APPLICATION_JSON);
        org.springframework.http.HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        // Gửi yêu cầu POST đến Flask API
        ResponseEntity<Map> response = restTemplate.exchange(flaskApiUrl, HttpMethod.POST, (org.springframework.http.HttpEntity<?>) entity, Map.class);

        // Xử lý phản hồi từ API
        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && "Toxic".equals(responseBody.get("Toxic"))) {
                return true;
            }
        } else {
            throw new RuntimeException("Error occurred while calling toxicity API: " + response.getStatusCode());
        }
        return false;
    }
}
