package com.example.demo.domain.auth.api;

import com.example.demo.domain.auth.application.AuthService;
import com.example.demo.domain.auth.dto.request.JoinRequest;
import com.example.demo.domain.auth.dto.request.ValidateEmailRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping(value = "/sign-up", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> join(@RequestBody @Valid JoinRequest request) {
        authService.join(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PostMapping("/duplicate")
    public ResponseEntity<Void> checkEmail(@RequestBody @Valid ValidateEmailRequest request) {
        boolean notExistEmail = authService.isNotExistEmail(request.getEmail());

        return ResponseEntity.
                noContent()
                .build();
    }
}
