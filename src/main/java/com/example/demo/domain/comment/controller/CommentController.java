package com.example.demo.domain.comment.controller;


import com.example.demo.domain.comment.domain.request.CommentRequest;
import com.example.demo.domain.comment.domain.response.CommentInfo;
import com.example.demo.domain.comment.domain.response.CommentPageResponse;
import com.example.demo.domain.comment.domain.response.CommentResponse;
import com.example.demo.domain.comment.service.CommentService;
import com.example.demo.domain.study_project_board.domain.dto.vo.BoardCategory;
import com.example.demo.global.aop.AssignUserId;
import com.example.demo.global.base.dto.ResponseBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.global.base.dto.ResponseUtil.createSuccessResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentController {
    private final CommentService commentService;


    /**
     * 게시물 별 댓글 조회
     *
     * @param : boardCategory[study, project, seminar]
     */
    @GetMapping("/{boardId}")
    public ResponseEntity<ResponseBody<CommentResponse>> getBoardComments(
            @PathVariable Long boardId,
            @RequestParam String boardCategory) {
        // TODO : 내가 차단한 사용자 댓글은 보이지 않도록(로그인했을 시)
        return ResponseEntity.ok(createSuccessResponse(commentService.findCommentsByBoardId(boardId, BoardCategory.valueOf(boardCategory.toUpperCase()))));
    }

    /**
     * 사용자 작성 댓글 조회
     *
     * @param : boardCategory[study, project, seminar]
     */
    @AssignUserId
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my-comments")
    public ResponseEntity<ResponseBody<CommentPageResponse>> getUserComments(
            Long userId,
            @PageableDefault(page = 1, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam String boardCategory) {
        return ResponseEntity.ok(createSuccessResponse(commentService.findCommentsByUserId(userId, BoardCategory.valueOf(boardCategory.toUpperCase()), pageable)));
    }

    /**
     * 댓글 저장
     *
     * @param : boardCategory[study, project, seminar]
     */
    @AssignUserId
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{boardId}")
    public ResponseEntity<ResponseBody<CommentInfo>> createComment(Long userId, @RequestBody @Valid CommentRequest commentRequest,
                                                                   @PathVariable Long boardId, @RequestParam String boardCategory) {
        // 댓글 작성시 사용자 권한 확인
        return ResponseEntity.ok(createSuccessResponse(commentService.saveComment(commentRequest, userId, boardId, BoardCategory.valueOf(boardCategory.toUpperCase()))));
    }

    /**
     * 댓글 수정
     */
    @AssignUserId
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{commentId}")
    public ResponseEntity<ResponseBody<CommentInfo>> updateComment(Long userId,
                                                                   @RequestBody @Valid CommentRequest commentRequest,
                                                                   @PathVariable Long commentId) {
        return ResponseEntity.ok(createSuccessResponse(commentService.updateComment(commentRequest, commentId, userId)));
    }

    /**
     * 댓글 삭제
     */
    @AssignUserId
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ResponseBody<Void>> deleteComment(Long userId, @PathVariable Long commentId) {
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.ok().body(createSuccessResponse());
    }

    // TODO : ADMIN 기능
}
