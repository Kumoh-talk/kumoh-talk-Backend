package com.example.demo.domain.comment.controller.swagger;

import com.example.demo.domain.comment.controller.FakeLoginController;
import com.example.demo.global.base.dto.ResponseBody;
import com.example.demo.global.config.swagger.ApiResponseExplanations;
import com.example.demo.global.config.swagger.ApiSuccessResponseExplanation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface FakeLoginApi {
    @Operation(
            summary = "임시 JWT 토큰 발급",
            description = "로그인을 대신하여 Admin 권한을 가진 JWT 토큰을 발급받습니다."
    )
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = FakeLoginController.LoginResponse.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = FakeLoginController.LoginResponse.class,
                    description = "임시 JWT 토큰 발급 성공")
    )
    ResponseEntity<ResponseBody<FakeLoginController.LoginResponse>> fakeLogin();
}
