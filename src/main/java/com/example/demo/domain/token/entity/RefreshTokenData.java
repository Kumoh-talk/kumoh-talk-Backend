package com.example.demo.domain.token.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefreshTokenData {
    private final Long userId;
    private final String refreshToken;

    public RefreshTokenData(Long userId, String refreshToken) {
        this.userId = userId;
        this.refreshToken = refreshToken;
    }
}
