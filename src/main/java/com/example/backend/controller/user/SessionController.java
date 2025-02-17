package com.example.backend.controller.user;

import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/session")
public class SessionController {
    @GetMapping("/getSessionId")
    public String getSessionId(@CookieValue(name = "SESSION_ID", required = false) String sessionId) {
        if (sessionId == null || sessionId.isEmpty()) {
            sessionId = UUID.randomUUID().toString(); // Tạo sessionId mới
        }
        return sessionId;
    }
}

