package com.example.demo.domain.comment.controller;


import com.example.demo.domain.board.domain.dto.vo.BoardType;
import com.example.demo.domain.comment.controller.swagger.AbstractBoardCommentApi;
import com.example.demo.domain.comment.domain.dto.request.CommentRequest;
import com.example.demo.domain.comment.domain.dto.response.CommentInfoResponse;
import com.example.demo.domain.comment.domain.dto.response.CommentResponse;
import com.example.demo.domain.comment.domain.dto.response.MyCommentResponse;
import com.example.demo.domain.comment.service.AbstractCommentService;
import com.example.demo.domain.notification.domain.vo.NotificationType;
import com.example.demo.domain.recruitment_board.domain.vo.EntireBoardType;
import com.example.demo.global.aop.AssignUserId;
import com.example.demo.global.base.dto.ResponseBody;
import com.example.demo.global.base.dto.page.GlobalPageResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.global.base.dto.ResponseUtil.createSuccessResponse;

@RestController
@RequestMapping("/api/v1/board/comments")
public class BoardCommentController extends AbstractBoardCommentApi {
    private final AbstractCommentService commentService;

    public BoardCommentController(
            @Qualifier("boardCommentService") AbstractCommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * [마크다운 게시글 댓글 조회] <br>
     * 마크다운 게시물 id에 해당하는 댓글 조회
     *
     * @apiNote 1. 삭제된 댓글도 응답으로 보낸다 <br>
     * -> 프론트에서 deletedAt 널 체크 후 "삭제되었습니다"로 내용 변경이 필요 <br>
     * 2. 기획의 요청에 따른 페이징 처리 X <br>
     */
    @Override
    @GetMapping("/{boardId}")
    public ResponseEntity<ResponseBody<CommentResponse>> getBoardComments(
            @PathVariable Long boardId) {
        // TODO : 내가 차단한 사용자 댓글은 보이지 않도록(로그인했을 시)
        return ResponseEntity.ok(createSuccessResponse(commentService.findCommentsByBoardId(boardId)));
    }

    /**
     * [마크다운 게시물의 사용자 작성 댓글 조회] <br>
     * 사용자 정보 창에서 마크다운 게시판 타입 선택 시 타입에 해당하는 게시물에 작성했던 사용자의 댓글 조회
     *
     * @param boardType [SEMINAR, NOTICE]
     * @apiNote 1. 날짜순으로 내림차순 정렬 하드코딩 해놨음 <br>
     * 2. 쿼리파라미터의 BoardType은 대소문자 따지지않고 스펠링만 맞으면 역직렬화 성공하도록 구현
     */
    @Override
    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_USER')")
    @GetMapping("/my-comments")
    public ResponseEntity<ResponseBody<GlobalPageResponse<MyCommentResponse>>> getUserComments(
            Long userId,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam BoardType boardType) {
        return ResponseEntity.ok(createSuccessResponse(commentService.findCommentsByUserId(userId, pageable, EntireBoardType.fromBoardType(boardType))));
    }

    /**
     * [마크다운 게시물 댓글 저장] <br>
     * 게시글에서 댓글 작성 후 저장
     */
    @Override
    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_USER')")
    @PostMapping("/{boardId}")
    public ResponseEntity<ResponseBody<CommentInfoResponse>> createComment(
            Long userId,
            @PathVariable Long boardId,
            @RequestBody @Valid CommentRequest commentRequest) {
        return ResponseEntity.ok(createSuccessResponse(commentService.saveComment(userId, boardId, commentRequest, NotificationType.BOARD_COMMENT)));
    }

    /**
     * [마크다운 게시물 댓글 수정] <br>
     * 작성한 댓글 수정
     */
    @Override
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
     * [마크다운 게시물 댓글 삭제] <br>
     * 작성한 댓글 soft 삭제
     *
     * @apiNote 1. 삭제된 댓글도 응답으로 보내야하므로 Comment 엔티티에 SQLRestriction 처리를 해놓지 않았음 <br>
     * 2. 관리자 기능과 달리 로그인한 유저의 id와 댓글 작성 유저의 id를 비교하는 절차를 거쳐야하므로, isAuthorized 매개변수를 false로 설정함
     */
    @Override
    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_USER')")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ResponseBody<Void>> deleteComment(Long userId, @PathVariable Long commentId) {
        commentService.deleteComment(userId, commentId, false);
        return ResponseEntity.ok().body(createSuccessResponse());
    }
}