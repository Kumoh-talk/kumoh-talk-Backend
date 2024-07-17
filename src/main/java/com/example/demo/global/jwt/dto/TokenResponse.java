package com.example.demo.global.jwt.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
