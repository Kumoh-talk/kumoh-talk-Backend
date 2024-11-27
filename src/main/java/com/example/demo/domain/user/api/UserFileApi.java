package com.example.demo.domain.user.api;

import com.example.demo.domain.user.domain.dto.request.ChangeProfileUrlRequest;
import com.example.demo.domain.user.domain.dto.request.ProfilePresignedUrlRequest;
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
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserFileApi {
    @Operation(
            summary = "사용자 프로필 이미지 업로드를 위한 Presigned URL 발급 api",
            description = "USER 권한 이상의 사용자에 대해서 Presigned URL을 발급합니다."
    )
    @ApiResponse( content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = String.class,
                    description = "USER 권한 이상의 사용자가 사용할 수 있습니다.\n"
                            + "Presigned URL 을 발급합니다."),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.USER_NOT_FOUND),
            }
    )
    ResponseEntity<ResponseBody<String>> getPresignedUrl(@Parameter(hidden = true) Long userId,
                                                         @RequestBody @Valid ProfilePresignedUrlRequest request);

    @Operation(
            summary = "사용자 프로필 이미지 변경 api",
            description = "USER 권한 이상의 사용자에 대해서 이미지 URL을 변경합니다."
    )
    @ApiResponse( content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    description = "USER 권한 이상의 사용자가 사용할 수 있습니다.\n"
                            + "사용자 프로필 이미지를 변경합니다."),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.USER_NOT_FOUND),
            }
    )
    ResponseEntity<ResponseBody<Void>> changeProfileUrl(@Parameter(hidden = true) Long userId,
                                                        @RequestBody @Valid ChangeProfileUrlRequest request);

    @Operation(
            summary = "사용자 프로필 이미지 삭제 api",
            description = "USER 권한 이상의 사용자에 대해서 이미지를 삭제합니다."
    )
    @ApiResponse( content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    description = "USER 권한 이상의 사용자가 사용할 수 있습니다.\n"
                            + "사용자 프로필이미지를 삭제하면, 기본 이미지로 설정됩니다."),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.USER_NOT_FOUND),
            }
    )
    ResponseEntity<ResponseBody<Void>> deleteProfileImage(@Parameter(hidden = true) Long userId);
}
