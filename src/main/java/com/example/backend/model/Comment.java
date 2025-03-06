package com.example.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(
        "comments"
)
@Data
public class Comment {
    @Id
    private String id;
    private String description;
    private LocalDateTime createdAt = LocalDateTime.now();
    private String userId;
    private String productId;
    private List<ReplyComment> replyComment;
    private LocalDateTime updatedAt;
}
