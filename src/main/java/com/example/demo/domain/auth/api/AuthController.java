package com.example.demo.domain.auth.api;

import static com.example.demo.global.base.dto.ResponseUtil.*;

import com.example.demo.domain.auth.application.AuthService;
import com.example.demo.domain.auth.dto.request.JoinRequest;
import com.example.demo.domain.auth.dto.request.LoginRequest;
import com.example.demo.domain.auth.dto.response.LoginResponse;
import com.example.demo.global.base.dto.ResponseBody;
import com.example.demo.global.regex.UserRegex;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
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

    @GetMapping("/nickname-duplicate")
    public ResponseEntity<ResponseBody<Void>> nicknameDuplicateCheck(@RequestParam @Pattern(regexp = UserRegex.NICKNAME_REGEXP, message = "닉네임 형식이 맞지 않습니다.") String nickname ) {
        authService.nicknameDuplicateCheck(nickname);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(createSuccessResponse());
    }

    @PostMapping(value = "/sign-up")
    public ResponseEntity<ResponseBody<Void>> join(@RequestBody @Valid JoinRequest request) {
        authService.join(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createSuccessResponse());
    }

    @PostMapping(value = "/login")
    public ResponseEntity<ResponseBody<LoginResponse>> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse response = authService.login(request);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(createSuccessResponse(response));
    }
}
