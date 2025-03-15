package com.example.demo.application.comment.api;

import com.example.demo.application.comment.dto.request.CommentRequest;
import com.example.demo.application.comment.dto.response.CommentInfoResponse;
import com.example.demo.application.comment.dto.response.CommentResponse;
import com.example.demo.application.comment.dto.response.MyCommentResponse;
import com.example.demo.domain.recruitment_board.entity.vo.RecruitmentBoardType;
import com.example.demo.global.base.dto.ResponseBody;
import com.example.demo.global.base.dto.page.GlobalPageResponse;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.config.swagger.ApiErrorResponseExplanation;
import com.example.demo.global.config.swagger.ApiResponseExplanations;
import com.example.demo.global.config.swagger.ApiSuccessResponseExplanation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public abstract class AbstractRecruitmentBoardCommentApi implements CommentApi<RecruitmentBoardType> {
    @Override
    @Operation(
            summary = "모집 게시물 댓글 리스트 조회",
            description = "모집 게시글의 댓글 리스트를 조회합니다."
    )
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentResponse.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = CommentResponse.class,
                    description = "모집 게시물 댓글 리스트 조회 성공\n"
                            + "댓글은 페이징 처리되지 않습니다\n"
                            + "deletedAt가 null이 아닌 댓글은 닉네임을 \"(삭제)\"로, 댓글 내용을 \"삭제된 댓글입니다.\"로 수정하여 출력해야합니다."),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.BOARD_NOT_FOUND)
            }
    )
    public abstract ResponseEntity<ResponseBody<CommentResponse>> getBoardComments(
            @PathVariable Long boardId);

    @Override
    @Operation(
            summary = "사용자 작성 모집 게시물 대상 댓글 페이지 조회",
            description = "사용자의 모집 게시물 대상 댓글 페이지를 조회합니다."
    )
    @ApiResponse(content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = MyCommentResponse.class))
    )
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = GlobalPageResponse.class,
                    description = "모집 게시물 대상 사용자 댓글 페이지 조회 성공"),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.USER_NOT_FOUND)
            }
    )
    public abstract ResponseEntity<ResponseBody<GlobalPageResponse<MyCommentResponse>>> getUserComments(
            @Parameter(hidden = true) Long userId,
            @ParameterObject @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @Parameter(description = "모집 게시물 타입(study, project, mentoring)", example = "study") @RequestParam RecruitmentBoardType boardType);

    @Override
    @Operation(
            summary = "모집 게시물 댓글 저장",
            description = "모집 게시글에 댓글을 저장합니다."
    )
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentInfoResponse.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = CommentInfoResponse.class,
                    description = "모집 게시물 댓글 저장 성공"),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.USER_NOT_FOUND),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.PARENT_NOT_FOUND),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.BOARD_NOT_FOUND),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.INVALID_INPUT_VALUE)
            }
    )
    public abstract ResponseEntity<ResponseBody<CommentInfoResponse>> postComment(
            @Parameter(hidden = true) Long userId,
            @PathVariable Long boardId,
            @RequestBody @Valid CommentRequest commentRequest);

    @Override
    @Operation(
            summary = "모집 게시물 댓글 수정",
            description = "commentId에 해당하는 모집 게시물의 댓글을 수정합니다."
    )
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentInfoResponse.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = CommentInfoResponse.class,
                    description = "모집 게시물 댓글 수정 성공"),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.COMMENT_NOT_FOUND),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.ACCESS_DENIED),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.INVALID_INPUT_VALUE)
            }
    )
    public abstract ResponseEntity<ResponseBody<CommentInfoResponse>> patchComment(
            @Parameter(hidden = true) Long userId,
            @PathVariable Long commentId,
            @RequestBody @Valid CommentRequest commentRequest);

    @Override
    @Operation(
            summary = "모집 게시물 댓글 삭제",
            description = "사용자가 작성한 commentId에 해당하는 모집 게시물 댓글을 삭제합니다."
    )
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    description = "모집 게시물 댓글 삭제 성공"),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.COMMENT_NOT_FOUND),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.ACCESS_DENIED)
            }
    )
    public abstract ResponseEntity<ResponseBody<Void>> deleteComment(
            @Parameter(hidden = true) Long userId,
            @PathVariable Long commentId);
}
