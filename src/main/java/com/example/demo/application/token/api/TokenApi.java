package com.example.demo.application.token.api;

import com.example.demo.application.token.dto.TokenRequest;
import com.example.demo.application.token.dto.TokenResponse;
import com.example.demo.global.base.dto.ResponseBody;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.config.swagger.ApiErrorResponseExplanation;
import com.example.demo.global.config.swagger.ApiResponseExplanations;
import com.example.demo.global.config.swagger.ApiSuccessResponseExplanation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface TokenApi {
    @Operation(
            summary = "토큰 refresh api",
            description = "모든 사용자가 자신에 토큰에 대한 refresh 요청이 가능합니다."
    )
    @ApiResponse( content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenResponse.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = TokenResponse.class,
                    description = "모든 사용자가 사용할 수 있습니다.\n"
                            + "기존 access, refresh 토큰으로 재발급을 요청하면 새로운 access, refresh 토큰을 발급해줍니다."),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.JWT_INVALID),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.REFRESH_TOKEN_NOT_EXIST),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.TOKEN_NOT_MATCHED),
            }
    )
    ResponseEntity<ResponseBody<TokenResponse>> refresh(@RequestBody @Valid TokenRequest tokenRequest);
}
