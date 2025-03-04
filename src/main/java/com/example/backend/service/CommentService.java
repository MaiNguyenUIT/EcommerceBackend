package com.example.backend.service;

import com.example.backend.DTO.CommentDTO;
import com.example.backend.model.Comment;
import com.example.backend.model.ReplyComment;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CommentService {
    CommentDTO createComment(String userId, String productId, CommentDTO commentDTO);
    CommentDTO updateComment(String userId, String commentId, CommentDTO commentDTO);
    void deleteComment(String id);
    Page<Comment> getAllCommentByProductId(String productId, int page, int size, String sortField, String sortDirection);
    CommentDTO replyComment(String id, ReplyComment replyComment);
}
