package com.example.backend.controller.user;

import com.example.backend.DTO.CommentDTO;
import com.example.backend.model.Comment;
import com.example.backend.model.User;
import com.example.backend.service.CommentService;
import com.example.backend.service.UserService;
import com.example.backend.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;

    @PostMapping()
    public ResponseEntity<ApiResponse<CommentDTO>> createComment(@RequestHeader("Authorization") String jwt, @RequestParam String productId, @RequestBody CommentDTO commentDTO) throws Exception{
        User user = userService.findUserByJwtToken(jwt);
        CommentDTO comment = commentService.createComment(user.getId(), productId, commentDTO);
        ApiResponse<CommentDTO> apiResponse = ApiResponse.<CommentDTO>builder()
                .status(200)
                .data(comment)
                .message("Create comment successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentDTO>> updateComment(@RequestHeader("Authorization") String jwt,
                                                                 @PathVariable String id,
                                                                 @RequestBody CommentDTO commentDTO) throws Exception{
        User user = userService.findUserByJwtToken(jwt);
        CommentDTO comment = commentService.updateComment(user.getId(), id, commentDTO);
        ApiResponse<CommentDTO> apiResponse = ApiResponse.<CommentDTO>builder()
                .status(200)
                .data(comment)
                .message("Update comment successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentDTO>> deleteComment(@RequestHeader("Authorization") String jwt,
                                                                 @PathVariable String id) throws Exception{
        User user = userService.findUserByJwtToken(jwt);
        commentService.deleteComment(id);
        ApiResponse<CommentDTO> apiResponse = ApiResponse.<CommentDTO>builder()
                .status(200)
                .message("Delete comment with id: " + id + " successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<Page<Comment>>> getProductComment(
                                                                    @RequestParam String productId,
                                                                    @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size,
                                                                    @RequestParam(defaultValue = "time") String sortField,
                                                                    @RequestParam(defaultValue = "desc") String sortDirection) throws Exception{
        Page<Comment> comments = commentService.getAllCommentByProductId(productId, page, size, sortField, sortDirection);
        ApiResponse<Page<Comment>> apiResponse = ApiResponse.<Page<Comment>>builder()
                .status(200)
                .message("Get product comment successfully")
                .data(comments)
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
