package com.example.demo.domain.comment.controller;

import com.example.demo.domain.token.domain.dto.TokenResponse;
import com.example.demo.domain.user.domain.vo.Role;
import com.example.demo.global.base.dto.ResponseBody;
import com.example.demo.global.jwt.JwtHandler;
import com.example.demo.global.jwt.JwtUserClaim;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.demo.global.base.dto.ResponseUtil.createSuccessResponse;

@RestController
@RequestMapping("/api/test/auth/login")
@RequiredArgsConstructor
public class FakeLoginController {
    private final JwtHandler jwtHandler;

    /**
     * [임시 JWT 토큰을 발급받기] <br>
     * 개발 후 권한이 필요한 기능을 테스트를 해보고 싶을 때, 로그인을 하지 않고 JWT를 발급받을 수 있는 Api <br>
     *
     * @apiNote 1. 가짜 유저 아이디 : 1L, 가짜 유저 권한 : ADMIN <br>
     * 2. 제대로 된 사용을 위해 유저 아이디가 1L인 유저에 대한 추가 정보를 데이터베이스에 입력시켜놓아야 한다.
     */
    @PostMapping
    public ResponseEntity<ResponseBody<LoginResponse>> fakeLogin() {
        JwtUserClaim claim = new JwtUserClaim(1L, Role.ROLE_ADMIN);
        TokenResponse tokens = jwtHandler.createTokens(claim);
        LoginResponse response = new LoginResponse(tokens.accessToken());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(createSuccessResponse(response));
    }

    @Getter
    public static class LoginResponse {
        private String token;

        // 기본 생성자
        public LoginResponse() {
        }

        public LoginResponse(String token) {
            this.token = token;
        }
    }
}
