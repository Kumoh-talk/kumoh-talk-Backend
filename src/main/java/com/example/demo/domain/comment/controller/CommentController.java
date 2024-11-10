package com.example.demo.domain.comment.controller;


import com.example.demo.domain.comment.domain.dto.request.CommentRequest;
import com.example.demo.domain.comment.domain.dto.response.CommentInfoResponse;
import com.example.demo.domain.comment.domain.dto.response.CommentPageResponse;
import com.example.demo.domain.comment.domain.dto.response.CommentResponse;
import com.example.demo.domain.comment.domain.vo.CommentTargetBoardType;
import com.example.demo.domain.comment.service.CommentService;
import com.example.demo.domain.recruitment_board.domain.vo.BoardType;
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
     * [게시물 별 댓글 조회]<br>
     * 게시물 id에 해당하는 댓글 조회
     *
     * @param boardType [BASIC, RECRUITMENT]
     * @apiNote 1. 삭제된 댓글도 응답으로 보낸다 <br>
     * -> 프론트에서 deletedAt 널 체크 후 "삭제되었습니다"로 내용 변경이 필요 <br>
     * 2. 기획의 요청에 따른 페이징 처리 X <br>
     * 3. 쿼리파라미터의 CommentTargetBoardType는 대소문자 따지지않고 스펠링만 맞으면 역직렬화 성공하도록 구현
     */
    @GetMapping("/{boardId}")
    public ResponseEntity<ResponseBody<CommentResponse>> getBoardComments(
            @PathVariable Long boardId,
            @RequestParam CommentTargetBoardType boardType) {
        // TODO : 내가 차단한 사용자 댓글은 보이지 않도록(로그인했을 시)
        return ResponseEntity.ok(createSuccessResponse(commentService.findCommentsByBoardId(boardId, boardType)));
    }

    /**
     * [사용자 작성 댓글 조회] <br>
     * 사용자 정보 창에서 게시판 타입 선택 시 타입에 해당하는 게시물에 작성했던 사용자의 댓글 조회
     *
     * @param boardType [STUDY, PROJECT, MENTORING, SEMINAR_NOTICE, SEMINAR_SUMMARY]
     * @apiNote 1. QueryDsl 에 정렬 조건 넣기가 까다로워서 일단 날짜순으로 내림차순 정렬 하드코딩 해놨음 <br>
     * 2. 쿼리파라미터의 BoardType은 대소문자 따지지않고 스펠링만 맞으면 역직렬화 성공하도록 구현
     */
    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_USER')")
    @GetMapping("/my-comments")
    public ResponseEntity<ResponseBody<CommentPageResponse>> getUserComments(
            Long userId,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam BoardType boardType) {
        return ResponseEntity.ok(createSuccessResponse(commentService.findCommentsByUserId(userId, pageable, boardType)));
    }

    /**
     * [댓글 저장] <br>
     * 게시글에서 댓글 작성 후 저장
     *
     * @param boardType [BASIC, RECRUITMENT]
     */
    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_USER')")
    @PostMapping("/{boardId}")
    public ResponseEntity<ResponseBody<CommentInfoResponse>> createComment(
            Long userId,
            @PathVariable Long boardId,
            @RequestParam CommentTargetBoardType boardType,
            @RequestBody @Valid CommentRequest commentRequest) {
        return ResponseEntity.ok(createSuccessResponse(commentService.saveComment(userId, boardId, boardType, commentRequest)));
    }

    /**
     * [댓글 수정] <br>
     * 작성한 댓글 수정
     */
    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_USER')")
    @PatchMapping("/{commentId}")
    public ResponseEntity<ResponseBody<CommentInfoResponse>> updateComment(
            Long userId,
            @PathVariable Long commentId,
            @RequestBody @Valid CommentRequest commentRequest) {
        return ResponseEntity.ok(createSuccessResponse(commentService.updateComment(userId, commentId, commentRequest)));
    }

    /**
     * [댓글 삭제] <br>
     * 작성한 댓글 soft 삭제
     *
     * @apiNote 1. 삭제된 댓글도 응답으로 보내야하므로 Comment 엔티티에 SQLRestriction 처리를 해놓지 않았음
     */
    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_USER')")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ResponseBody<Void>> deleteComment(Long userId, @PathVariable Long commentId) {
        commentService.deleteComment(userId, commentId, false);
        return ResponseEntity.ok().body(createSuccessResponse());
    }
}