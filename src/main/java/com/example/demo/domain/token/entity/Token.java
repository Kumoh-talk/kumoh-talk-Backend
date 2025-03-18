package com.example.demo.domain.token.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Token {
    private final String accessToken;
    private final String refreshToken;

    @Builder
    public Token(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
