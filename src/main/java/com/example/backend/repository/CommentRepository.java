package com.example.backend.repository;

import com.example.backend.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {
    Page<Comment> findByproductId(String productId, Pageable pageable);
}
