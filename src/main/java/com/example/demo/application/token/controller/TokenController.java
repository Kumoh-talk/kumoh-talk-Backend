package com.example.demo.application.token.controller;

import com.example.demo.application.token.api.TokenApi;
import com.example.demo.application.token.dto.TokenRequest;
import com.example.demo.application.token.dto.TokenResponse;
import com.example.demo.domain.token.entity.Token;
import com.example.demo.domain.token.service.TokenService;
import com.example.demo.global.base.dto.ResponseBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.demo.global.base.dto.ResponseUtil.createSuccessResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tokens")
public class TokenController implements TokenApi {

    private final TokenService tokenService;

    @PostMapping("/refresh")
    public ResponseEntity<ResponseBody<TokenResponse>> refresh(@RequestBody @Valid TokenRequest tokenRequest) {
        Token token = new Token(tokenRequest.accessToken(), tokenRequest.refreshToken());
        Token response = tokenService.refresh(token);
        return ResponseEntity.ok(createSuccessResponse(TokenResponse.create(response.getAccessToken(), response.getRefreshToken())));
    }
}
