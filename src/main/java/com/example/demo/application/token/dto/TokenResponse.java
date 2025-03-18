package com.example.demo.application.token.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
    public static TokenResponse create(String accessToken, String refreshToken) {
        return new TokenResponse(accessToken, refreshToken);
    }
}
