package com.example.demo.application.user_additional_info.api;

import com.example.demo.application.user_additional_info.dto.response.UserAdditionalInfoResponse;
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

public interface UserAdditionalInfoAdminApi {
    @Operation(
            summary = "[ADMIN] 사용자 추가정보 확인 api",
            description = "ADMIN 권한을 가진 사용자가 특정 사용자 추가 정보를 확인합니다."
    )
    @ApiResponse( content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserAdditionalInfoResponse.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = UserAdditionalInfoResponse.class,
                    description = "ADMIN 사용자가 사용가능합니다.\n"
                            + "특정 사용자의 추가 정보를 확인할 수 있습니다."),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.USER_NOT_FOUND),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.USER_ADDITIONAL_INFO_NOT_FOUND),
            }
    )
    ResponseEntity<ResponseBody<UserAdditionalInfoResponse>> getUserAdditionalInfo (@PathVariable Long userId);
}
