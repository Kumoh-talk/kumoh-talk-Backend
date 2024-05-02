package com.example.demo.domain.comment.controller;


import com.example.demo.domain.auth.domain.UserContext;
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

    /**
     * 게시물 댓글 조회
     * @param : boardId
     * @return : 전체 페이지 수, 현재 페이지 번호, 댓글 수, 댓글 관련내용(작성자, 대댓글 여부, 댓글 내용, 좋아요, 수정 날짜, 댓글 페이지)
     */
    @GetMapping("/find-comment/{boardId}")
    public ResponseEntity<List<CommentInfoResponse>> findCommentByPostId(@PathVariable Long boardId) {
        return ResponseEntity.ok(commentService.findByPostId(boardId));
    }

    /**
     * 댓글 저장
     * @param : user, commentRequest(content, parentId, ), boardId
     * @return : commentId, content, username, createdAt, parentId
     */
    @PostMapping("/save/{boardId}")
    public ResponseEntity<CommentInfoResponse> Save(@AuthenticationPrincipal UserContext user,
                                                    @RequestBody @Valid CommentRequest commentRequest,
                                                    @PathVariable Long boardId) {

        return ResponseEntity.ok(commentService.save(user.getId(), commentRequest, boardId));
    }

    /**
     * 댓글 수정
     * @param : user, commentRequest()
     * @return : commentId, content, username, updatedAt, parentId
     */
    @PatchMapping("/update/{commentId}")
    public ResponseEntity<CommentInfoResponse> qnaUpdate(@AuthenticationPrincipal UserContext user,
                                                         @RequestBody @Valid CommentRequest commentRequest,
                                                         @PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.update(commentRequest,commentId,user.getUsername()));
    }

    /**
     * 댓글 삭제(부모 댓글 삭제 시 삭제가 아닌 닉네임, 내용 교체만)
     * @param : commentId
     * @return : 응답코드
     */
    @PatchMapping("/delete/{commentId}")
    public ResponseEntity<Void> qnaDelete(@PathVariable Long commentId) {
        commentService.delete(commentId);
        return ResponseEntity.ok().build();
    }

    // 게시물 댓글 수 조회

    // 사용자가 작성한 댓글 조회

    // 대댓글 작성

    // 댓글 좋아요

    // 댓글 좋아요 삭제

}
