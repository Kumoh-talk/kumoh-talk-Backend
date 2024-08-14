package com.example.demo.domain.token.domain.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
    public static TokenResponse create(String accessToken, String refreshToken) {
        return new TokenResponse(accessToken, refreshToken);
    }
}
