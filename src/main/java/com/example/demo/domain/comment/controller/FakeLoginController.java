package com.example.demo.domain.comment.controller;

import com.example.demo.domain.token.domain.dto.TokenResponse;
import com.example.demo.domain.user.domain.vo.Role;
import com.example.demo.global.base.dto.ResponseBody;
import com.example.demo.global.jwt.JwtHandler;
import com.example.demo.global.jwt.JwtUserClaim;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    @PostMapping
    public ResponseEntity<ResponseBody<LoginResponse>> fakeLogin() {
        JwtUserClaim claim = new JwtUserClaim(1L, Role.ROLE_USER);
        TokenResponse tokens = jwtHandler.createTokens(claim);
        LoginResponse response = new LoginResponse(tokens.accessToken());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(createSuccessResponse(response));
    }

    @Getter
    @NoArgsConstructor
    public static class LoginResponse {
        private String token;

        public LoginResponse(String token) {
            this.token = token;
        }
    }
}
