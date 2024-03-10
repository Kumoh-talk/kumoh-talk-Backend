package com.example.demo.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponse {
    private String accessTokenType;
    private String accessToken;
    private int accessTokenExpirationTime;
    private String refreshTokenId;
    private int refreshTokenExpirationTime;

    @Builder
    public LoginResponse(String accessTokenType, String accessToken, String refreshTokenId) {
        this.accessTokenType = accessTokenType;
        this.accessToken = accessToken;
        this.accessTokenExpirationTime = 1800;
        this.refreshTokenId = refreshTokenId;
        this.refreshTokenExpirationTime = 1209600;
    }
}
