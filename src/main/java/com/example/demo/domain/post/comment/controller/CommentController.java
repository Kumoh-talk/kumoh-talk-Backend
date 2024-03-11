package com.example.demo.domain.post.comment.controller;


import com.example.demo.domain.auth.domain.UserPrincipal;
import com.example.demo.domain.post.comment.domain.request.CommentSaveRequest;
import com.example.demo.domain.post.comment.domain.request.CommentUpdateRequest;
import com.example.demo.domain.post.comment.domain.response.CommentInfoResponse;
import com.example.demo.domain.post.comment.service.CommentService;
import com.example.demo.domain.post.post_qna.domain.request.QnaSaveRequest;
import com.example.demo.domain.post.post_qna.domain.request.QnaUpdateRequest;
import com.example.demo.domain.post.post_qna.domain.response.QnaInfoResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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

    @PostMapping("/save")
    public ResponseEntity<CommentInfoResponse> Save(@AuthenticationPrincipal UserPrincipal user, @RequestBody @Valid CommentSaveRequest commentSaveRequest) {

        return ResponseEntity.ok(commentService.save(user.getId(), commentSaveRequest));
    }


    @PatchMapping("/update")
    public ResponseEntity<CommentInfoResponse> qnaUpdate(@RequestBody @Valid CommentUpdateRequest commentUpdateRequest) {

        return ResponseEntity.ok(commentService.update(commentUpdateRequest));
    }

    @PatchMapping("/delete")
    public ResponseEntity<Void> qnaDelete(@RequestParam @NotBlank(message = "Comment 고유id 누락") Long commentId) {
        commentService.delete(commentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/find-comment")
    public ResponseEntity<List<CommentInfoResponse>> findCommentByPostId(@RequestParam @NotBlank(message = "게시글 고유id 누락") Long postId) {
        return ResponseEntity.ok(commentService.findByPostId(postId));
    }




}
