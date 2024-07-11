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
@RequestMapping("/api/v1/comments")
public class CommentController {
    private final CommentService commentService;

    /**
     * 게시물 댓글 조회
     * 요청 URL : /api/v1/comments/{boardId}
     * @param :
     * @return : 댓글 전체 수, 각 댓글 관련 정보(댓글 아이디, 작성자 닉네임, 대댓글 여부(그룹 아이디), 댓글 내용, 대댓글 리스트)
     */
    @GetMapping("/{boardId}")
    public ResponseEntity<ResponseBody<CommentResponse>> getComments(@PathVariable Long boardId) {
        return ResponseEntity.ok(createSuccessResponse(commentService.findByBoardId(boardId)));
    }

    /**
     * 댓글 저장
     * 요청 URL : /api/v1/comments/{boardId}
     * @param : 작성 댓글 정보(내용, 대댓글 여부(그룹 아이디))
     * @return : 작성 댓글 관련 정보(댓글 아이디, 대댓글 여부(그룹 아이디), 작성자 닉네임, 댓글 내용, 대댓글 리스트)
     */
    @AssignUserId
    @PostMapping("/{boardId}")
    public ResponseEntity<ResponseBody<CommentInfo>> createComment(Long userId, @RequestBody @Valid CommentRequest commentRequest, @PathVariable Long boardId) {
        // 댓글 작성시 사용자 권한 확인
        return ResponseEntity.ok(createSuccessResponse(commentService.save(userId, commentRequest, boardId)));
    }

    /**
     * 댓글 수정
     * 요청 URL : /api/v1/comments/{commentId}
     * @param : 작성 댓글 정보(내용, 대댓글 여부(그룹 아이디))
     * @return : 수정 댓글 관련 정보(댓글 아이디, 대댓글 여부(그룹 아이디), 작성자 닉네임, 댓글 내용, 대댓글 리스트)
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
     * 요청 URL : /api/v1/comments/{commentId}
     * @param :
     * @return : 응답코드
     */
    @AssignUserId
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ResponseBody<Void>> deleteComment(Long userId, @PathVariable Long commentId) {
        commentService.delete(commentId, userId);
        return ResponseEntity.ok().body(createSuccessResponse());
    }

    // 사용자 댓글 조회?

}
