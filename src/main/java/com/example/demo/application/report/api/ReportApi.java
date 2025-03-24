package com.example.demo.application.report.api;

import com.example.demo.global.base.dto.ResponseBody;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.config.swagger.ApiErrorResponseExplanation;
import com.example.demo.global.config.swagger.ApiResponseExplanations;
import com.example.demo.global.config.swagger.ApiSuccessResponseExplanation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface ReportApi {
    @Operation(
            summary = "마크다운 게시물 댓글 신고하기 api",
            description = "[USER] 댓글을 신고합니다."
    )
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    description = "USER 이상의 권한을 가진 사용자에 대해서만 사용할 수 있습니다.\n"
                            + "댓글을 신고합니다."),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.COMMENT_NOT_FOUND),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.USER_NOT_FOUND),
            }
    )
    ResponseEntity<ResponseBody<Void>> reportBoardComment(@PathVariable Long commentId,
                                                          Long userId);

    @Operation(
            summary = "모집 게시물 댓글 신고하기 api",
            description = "[USER] 댓글을 신고합니다."
    )
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    description = "USER 이상의 권한을 가진 사용자에 대해서만 사용할 수 있습니다.\n"
                            + "댓글을 신고합니다."),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.COMMENT_NOT_FOUND),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.USER_NOT_FOUND),
            }
    )
    ResponseEntity<ResponseBody<Void>> reportRecruitmentBoardComment(@PathVariable Long commentId,
                                                                     Long userId);
}
