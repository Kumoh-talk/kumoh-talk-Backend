package com.example.demo.domain.auth.api;

import com.example.demo.domain.auth.application.AuthService;
import com.example.demo.domain.auth.dto.request.JoinRequest;
import com.example.demo.domain.auth.dto.request.LoginRequest;
import com.example.demo.domain.auth.dto.request.ValidateEmailRequest;
import com.example.demo.domain.auth.dto.response.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/emails/verification-requests")
    public ResponseEntity<Void> sendMessage(@RequestBody @Valid ValidateEmailRequest request) {
        authService.sendCodeToEmail(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PostMapping(value = "/sign-up", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> join(@RequestBody @Valid JoinRequest request) {
        authService.join(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    // TODO : 로그인 수정 필요 - to session
//    @PostMapping("/login")
//    public ResponseEntity<LoginResponse> login(HttpServletRequest httpServletRequest,
//                                               @RequestBody @Valid LoginRequest request) {
//        LoginResponse loginResponse = authService.login(httpServletRequest, request);
//
//        return ResponseEntity.ok(loginResponse);
//    }
}
