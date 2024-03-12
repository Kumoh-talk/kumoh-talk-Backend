package com.example.demo.domain.comment.controller;


import com.example.demo.domain.auth.domain.UserPrincipal;
import com.example.demo.domain.comment.domain.request.CommentRequest;
import com.example.demo.domain.comment.domain.response.CommentInfoResponse;
import com.example.demo.domain.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private CommentService commentService;

    @PostMapping("/save/{postId}")
    public ResponseEntity<CommentInfoResponse> Save(@AuthenticationPrincipal UserPrincipal user,
                                                    @RequestBody @Valid CommentRequest commentRequest,
                                                    @PathVariable Long postId) {

        return ResponseEntity.ok(commentService.save(user.getId(), commentRequest,postId));
    }


    @PatchMapping("/update/{commentId}")
    public ResponseEntity<CommentInfoResponse> qnaUpdate(@AuthenticationPrincipal UserPrincipal user,
                                                         @RequestBody @Valid CommentRequest commentRequest,
                                                         @PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.update(commentRequest,commentId,user.getUsername()));
    }

    @PatchMapping("/delete/{commentId}")
    public ResponseEntity<Void> qnaDelete(@PathVariable Long commentId) {
        commentService.delete(commentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/find-comment/{postId}")
    public ResponseEntity<List<CommentInfoResponse>> findCommentByPostId(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.findByPostId(postId));
    }

}
