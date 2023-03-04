package com.example.celog.comment.controller;

import com.example.celog.comment.dto.request.CommentRequestDto;
import com.example.celog.comment.dto.response.CommentResponseDto;
import com.example.celog.comment.service.CommentService;
import com.example.celog.common.SuccessResponse;
import com.example.celog.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/comments/{id}")
    public ResponseEntity<List<CommentResponseDto>> commentList(@PathVariable Long id) {
        return commentService.listComment(id);
    }

    @PostMapping("/comments/{id}")
    public ResponseEntity<CommentResponseDto> commentSave(@PathVariable Long id,
                                                          @RequestBody CommentRequestDto commentRequestDto,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.saveComment(id, commentRequestDto, userDetails);
    }

    @PutMapping("/comments/{comment-id}")
    public ResponseEntity<CommentResponseDto> commentModify(@PathVariable Long id,
                                                            @RequestBody CommentRequestDto commentRequestDto,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.modifyComment(id, commentRequestDto, userDetails);
    }

    @DeleteMapping("/comments/{comment-id}")
    public ResponseEntity<SuccessResponse> commentRemove(@PathVariable Long id,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.removeComment(id, userDetails);
    }

}
