package com.example.demo.domain.comment.controller;


import static com.example.demo.global.base.dto.ResponseUtil.*;

import com.example.demo.domain.comment.domain.request.CommentRequest;
import com.example.demo.domain.comment.domain.response.CommentInfo;
import com.example.demo.domain.comment.domain.response.CommentResponse;
import com.example.demo.domain.comment.service.CommentService;
import com.example.demo.global.aop.AssignUserId;
import com.example.demo.global.base.dto.ResponseBody;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ResponseBody<CommentResponse>> getBoardComments(@PathVariable Long boardId) {
        return ResponseEntity.ok(createSuccessResponse(commentService.findByBoardId(boardId)));
    }

    /**
     * 댓글 저장
     * 요청 URL : /api/comments/{boardId}
     * @param : user, commentRequest(content, groupId, depth), boardId
     * @return : commentId, content, username, createdAt, parentId
     */
    @AssignUserId
    @PostMapping("/{boardId}")
    public ResponseEntity<ResponseBody<CommentInfo>> createComment(Long userId,
                                                     @RequestBody @Valid CommentRequest commentRequest,
                                                     @PathVariable Long boardId) {

        return ResponseEntity.ok(createSuccessResponse(commentService.save(userId, commentRequest, boardId)));
    }

    /**
     * 댓글 수정
     * 요청 URL : /api/comments/{commentId}
     * @param : user, commentRequest()
     * @return : commentId, content, username, updatedAt, parentId
     */
    @AssignUserId
    @PatchMapping("/{commentId}")
    public ResponseEntity<ResponseBody<CommentInfo>> updateComment(Long userId,
                                                     @RequestBody @Valid CommentRequest commentRequest,
                                                     @PathVariable Long commentId) {
        return ResponseEntity.ok(createSuccessResponse(commentService.update(commentRequest,commentId,userId)));
    }

    /**
     * 댓글 삭제(부모 댓글 삭제 시 삭제가 아닌 닉네임, 내용 교체만)
     * 요청 URL : /api/comments/{commentId}
     * @param : commentId
     * @return : 응답코드
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ResponseBody<Void>> deleteComment(@PathVariable Long commentId) {
        commentService.delete(commentId);
        return ResponseEntity.ok().body(createSuccessResponse());
    }

    // 게시물 댓글 수 조회

    // 사용자가 작성한 댓글 조회

    // 대댓글 작성

    // 댓글 좋아요

    // 댓글 좋아요 삭제

}
