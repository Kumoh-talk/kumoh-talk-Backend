package com.example.demo.domain.comment.controller;

import com.example.demo.domain.comment.service.CommentService;
import com.example.demo.global.base.dto.ResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.demo.global.base.dto.ResponseUtil.createSuccessResponse;

@RestController
@RequestMapping("api/vi/admin/comments")
@RequiredArgsConstructor
public class AdminCommentController {
    private final CommentService commentService;

    /**
     * 관리자 전용 댓글 삭제
     */
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_USER')")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ResponseBody<Void>> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId, null, true);
        return ResponseEntity.ok().body(createSuccessResponse());
    }
}
