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
     * [관리자 전용 댓글 삭제] <br>
     * 작성한 댓글 soft 삭제
     *
     * @apiNote 1. 삭제된 댓글도 응답으로 보내야하므로 Comment 엔티티에 SQLRestriction 처리를 해놓지 않았음
     * 2. 필터에서 유저 권한이 ADMIN인 것을 확인하면 따로 서비스 로직에서 유저 인증을 거치지 않도록 isAuthorized 매개변수를 true로 하여 서비스 메서드를 호출
     */
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_USER')")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ResponseBody<Void>> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId, null, true);
        return ResponseEntity.ok().body(createSuccessResponse());
    }
}
