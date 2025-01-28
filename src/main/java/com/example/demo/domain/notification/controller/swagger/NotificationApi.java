package com.example.demo.domain.notification.controller.swagger;

import com.example.demo.domain.notification.domain.dto.response.NotificationNoOffsetResponse;
import com.example.demo.global.base.dto.ResponseBody;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.config.swagger.ApiErrorResponseExplanation;
import com.example.demo.global.config.swagger.ApiResponseExplanations;
import com.example.demo.global.config.swagger.ApiSuccessResponseExplanation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public interface NotificationApi {

    @Operation(
            summary = "알림 페이지 조회",
            description = "무한스크롤이 가능한 방식으로 사용자의 알림 페이지를 조회합니다."
    )
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotificationNoOffsetResponse.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = NotificationNoOffsetResponse.class,
                    description = "알림 페이지 조회 성공"),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.USER_NOT_FOUND)
            }
    )
    public ResponseEntity<ResponseBody<NotificationNoOffsetResponse>> getUserNotification(
            @Parameter(hidden = true) Long userId,
            @Parameter(description = "페이지 크기", example = "10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "이전 페이지의 가장 마지막 게시물 id(첫 페이지 조회 시 입력 x)", example = "3") @RequestParam(required = false) Long lastNotificationId);

    @Operation(
            summary = "알림 읽음 처리",
            description = "알림 클릭 시 알림을 읽음 처리합니다."
    )
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    description = "알림 읽음 처리 성공"),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.USER_NOT_FOUND),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.NOTIFICATION_NOT_FOUND),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.RECEIVER_NOT_MATCH)
            }
    )
    public ResponseEntity<ResponseBody<Void>> readUserNotification(
            @Parameter(hidden = true) Long userId,
            @PathVariable Long notificationId);

    @Operation(
            summary = "알림 삭제",
            description = "해당 알림을 삭제 처리합니다."
    )
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    description = "알림 삭제 성공"),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.USER_NOT_FOUND),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.NOTIFICATION_NOT_FOUND),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.RECEIVER_NOT_MATCH)
            }
    )
    public ResponseEntity<ResponseBody<Void>> deleteUserNotification(
            @Parameter(hidden = true) Long userId,
            @PathVariable Long notificationId);
}
