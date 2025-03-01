package com.example.demo.application.comment.api;

import com.example.demo.global.base.dto.ResponseBody;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.config.swagger.ApiErrorResponseExplanation;
import com.example.demo.global.config.swagger.ApiResponseExplanations;
import com.example.demo.global.config.swagger.ApiSuccessResponseExplanation;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface AdminCommentApi {
    @Operation(
            summary = "관리자 전용 마크다운 게시물 댓글 삭제",
            description = "관리자가 commentId에 해당하는 댓글을 삭제합니다."
    )
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    description = "관리자 댓글 삭제 성공"),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.COMMENT_NOT_FOUND)
            }
    )
    ResponseEntity<ResponseBody<Void>> deleteBoardComment(@PathVariable Long commentId);

    @Operation(
            summary = "관리자 전용 모집 게시물 댓글 삭제",
            description = "관리자가 commentId에 해당하는 댓글을 삭제합니다."
    )
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    description = "관리자 댓글 삭제 성공"),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.COMMENT_NOT_FOUND)
            }
    )
    ResponseEntity<ResponseBody<Void>> deleteRecruitmentBoardComment(@PathVariable Long commentId);
}
