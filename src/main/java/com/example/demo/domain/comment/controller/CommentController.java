package com.example.demo.domain.comment.controller;


import com.example.demo.domain.auth.domain.UserContext;
import com.example.demo.domain.comment.domain.request.CommentRequest;
import com.example.demo.domain.comment.domain.response.CommentInfo;
import com.example.demo.domain.comment.domain.response.CommentResponse;
import com.example.demo.domain.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;

    /**
     * 게시물 댓글 조회
     * 요청 URL : /api/comments/{boardId}
     * @param : boardId
     * @return : 댓글 수, 댓글 관련내용(작성자, 대댓글 여부(그룹 아이디), 댓글 내용, 좋아요, 수정 날짜)
     */
    @GetMapping("/{boardId}")
    public ResponseEntity<CommentResponse> getBoardComments(@PathVariable Long boardId) {
        return ResponseEntity.ok(commentService.findByBoardId(boardId));
    }

    /**
     * 댓글 저장
     * 요청 URL : /api/comments/{boardId}
     * @param : user, commentRequest(content, groupId, depth), boardId
     * @return : commentId, content, username, createdAt, parentId
     */
    @PostMapping("/{boardId}")
    public ResponseEntity<CommentInfo> createComment(@AuthenticationPrincipal UserContext user,
                                                     @RequestBody @Valid CommentRequest commentRequest,
                                                     @PathVariable Long boardId) {

        return ResponseEntity.ok(commentService.save(user.getId(), commentRequest, boardId));
    }

    /**
     * 댓글 수정
     * 요청 URL : /api/comments/{commentId}
     * @param : user, commentRequest()
     * @return : commentId, content, username, updatedAt, parentId
     */
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentInfo> updateComment(@AuthenticationPrincipal UserContext user,
                                                     @RequestBody @Valid CommentRequest commentRequest,
                                                     @PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.update(commentRequest,commentId,user.getUsername()));
    }

    /**
     * 댓글 삭제(부모 댓글 삭제 시 삭제가 아닌 닉네임, 내용 교체만)
     * 요청 URL : /api/comments/{commentId}
     * @param : commentId
     * @return : 응답코드
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.delete(commentId);
        return ResponseEntity.ok().build();
    }

    // 게시물 댓글 수 조회

    // 사용자가 작성한 댓글 조회

    // 대댓글 작성

    // 댓글 좋아요

    // 댓글 좋아요 삭제

}
