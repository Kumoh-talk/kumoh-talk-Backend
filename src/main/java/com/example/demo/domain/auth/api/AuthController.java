package com.example.demo.domain.auth.api;

import com.example.demo.domain.auth.application.AuthService;
import com.example.demo.domain.auth.dto.request.JoinRequest;
import com.example.demo.domain.auth.dto.request.ValidateEmailRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    // TODO. 이메일 인증 기능 링크 방법으로 변경해야 함.
    @PostMapping("/emails/verification-requests")
    public ResponseEntity<Void> sendMessage(@RequestBody @Valid ValidateEmailRequest request) {
        authService.sendCodeToEmail(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PostMapping(value = "/sign-up")
    public ResponseEntity<Void> join(@RequestBody @Valid JoinRequest request) {
        authService.join(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }
}
