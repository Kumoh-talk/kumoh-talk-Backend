package com.example.demo.global.jwt.controller;

import com.example.demo.global.base.dto.ResponseBody;
import com.example.demo.global.jwt.dto.TokenRequest;
import com.example.demo.global.jwt.dto.TokenResponse;
import com.example.demo.global.jwt.service.TokenService;
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
@RequestMapping("/api/tokens")
public class TokenController {

    private final TokenService tokenService;

    @PostMapping("/refresh")
    public ResponseEntity<ResponseBody<TokenResponse>> refresh(@RequestBody @Valid TokenRequest tokenRequest) {

        return ResponseEntity.ok(createSuccessResponse(tokenService.refresh(tokenRequest)));
    }
}
