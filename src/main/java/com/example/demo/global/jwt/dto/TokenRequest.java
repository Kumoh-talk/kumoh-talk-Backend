package com.example.demo.global.jwt.dto;

import jakarta.validation.constraints.NotBlank;

public record TokenRequest(
        @NotBlank(message = "accessToken 값이 비었습니다.")
        String accessToken,
        @NotBlank(message = "refreshToken 값이 비었습니다.")
        String refreshToken
) {
}
