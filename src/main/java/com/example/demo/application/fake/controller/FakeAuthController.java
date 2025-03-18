package com.example.demo.application.fake.controller;

import com.example.demo.application.fake.api.FakeAuthApi;
import com.example.demo.domain.fake.service.FakeAuthService;
import com.example.demo.domain.token.entity.Token;
import com.example.demo.global.base.dto.ResponseBody;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.demo.global.base.dto.ResponseUtil.createSuccessResponse;

@Profile({"local", "dev"})
@RestController
@RequestMapping("/api/test/auth/login")
@RequiredArgsConstructor
public class FakeAuthController implements FakeAuthApi {

    private final FakeAuthService fakeAuthService;

    /**
     * [가짜 계정 로그인] <br>
     * 개발 후 권한이 필요한 기능을 테스트를 해보고 싶을 때, 로그인을 하지 않고 JWT를 발급받을 수 있는 Api <br>
     *
     * @apiNote 1. 가짜 유저 권한 : ADMIN <br>
     */
    @PostMapping
    public ResponseEntity<ResponseBody<LoginResponse>> fakeLogin() {
        Token token = fakeAuthService.fakeLogin();
        FakeAuthController.LoginResponse response = new FakeAuthController.LoginResponse(token.getAccessToken());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(createSuccessResponse(response));
    }

    @Getter
    @Schema(name = "LoginResponse", description = "임시 JWT 토큰 발급 응답")
    public static class LoginResponse {
        @Schema(description = "JWT 토큰", example = "\"eyJhbGciOiJIUzI1NiJ9.eyJVU0VSX0lEIjoxLCJVU0VSX1JPTEUiOiJST0xFX0FETUlOIiwiaWF0IjoxNzMxOTQ0OTAxLCJleHAiOjE3MzE5NDY3MDF9.iLoB5n0v_xSUCvZnqdWUtrhQnO_8UZGHfxWJKqJJIys\"")
        private String token;

        // 기본 생성자
        public LoginResponse() {
        }

        public LoginResponse(String token) {
            this.token = token;
        }
    }
}
