package com.example.backend.controller.admin;

import com.example.backend.DTO.CommentDTO;
import com.example.backend.model.ReplyComment;
import com.example.backend.model.User;
import com.example.backend.service.CommentService;
import com.example.backend.service.UserService;
import com.example.backend.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin/comment")
public class AdminCommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;

    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentDTO>> replyComment(@RequestHeader("Authorization") String jwt,
                                                                @RequestBody ReplyComment replyComment,
                                                                @PathVariable String id) throws Exception{
        User user = userService.findUserByJwtToken(jwt);
        CommentDTO commentDTO = commentService.replyComment(id, replyComment);
        ApiResponse<CommentDTO> apiResponse = ApiResponse.<CommentDTO>builder()
                .data(commentDTO)
                .message("Reply comment successfully")
                .status(200)
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
