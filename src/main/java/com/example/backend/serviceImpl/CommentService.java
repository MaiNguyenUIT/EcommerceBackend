package com.example.backend.serviceImpl;

import com.example.backend.DTO.CommentDTO;
import com.example.backend.exception.BadRequestException;
import com.example.backend.exception.NotFoundException;
import com.example.backend.mapper.CommentMapper;
import com.example.backend.model.Comment;
import com.example.backend.model.Product;
import com.example.backend.model.ReplyComment;
import com.example.backend.repository.CommentRepository;
import com.example.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class CommentService implements com.example.backend.service.CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CheckToxicService checkToxicService;
    @Override
    public CommentDTO createComment(String userId, String productId, CommentDTO commentDTO) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + productId));
        Comment comment = CommentMapper.INSTANCE.toEntity(commentDTO);
        comment.setUserId(userId);
        comment.setProductId(productId);

        if(checkToxicService.checkToxic(commentDTO.getDescription())){
            throw new BadRequestException("Comment contains toxic content and cannot be submitted.");
        } else {
            commentRepository.save(comment);
        }
        return commentDTO;
    }

    @Override
    public CommentDTO updateComment(String userId, String commentId, CommentDTO commentDTO) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment is not exist with id: " + commentId));
        if(!Objects.equals(comment.getUserId(), userId)){
            throw new BadRequestException("You cannot update other people's comments");
        }
        if(checkToxicService.checkToxic(commentDTO.getDescription())){
            throw new BadRequestException("Comment contains toxic content and cannot be submitted.");
        } else {
            comment.setDescription(commentDTO.getDescription());
            comment.setUpdatedAt(LocalDateTime.now());
            commentRepository.save(comment);
        }
        return CommentMapper.INSTANCE.toDTO(comment);
    }

    @Override
    public void deleteComment(String id) {
        commentRepository.deleteById(id);
    }

    @Override
    public Page<Comment> getAllCommentByProductId(String productId, int page, int size, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("desc") ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return commentRepository.findByproductId(productId, pageable);
    }

    @Override
    public CommentDTO replyComment(String id, ReplyComment replyComment) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comment is not exist with id: " + id));
        comment.getReplyComment().add(replyComment);
        return CommentMapper.INSTANCE.toDTO(commentRepository.save(comment));
    }
}
