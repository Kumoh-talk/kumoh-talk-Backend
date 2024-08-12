package com.example.demo.global.jwt.service;

import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.global.jwt.JwtHandler;
import com.example.demo.global.jwt.JwtUserClaim;
import com.example.demo.global.jwt.domain.RefreshToken;
import com.example.demo.global.jwt.dto.TokenRequest;
import com.example.demo.global.jwt.dto.TokenResponse;
import com.example.demo.global.jwt.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtHandler jwtHandler;

    public TokenResponse refresh(TokenRequest tokenRequest) {
        JwtUserClaim jwtUserClaim = jwtHandler.getClaims(tokenRequest.accessToken())
                .orElseThrow(() -> new ServiceException(ErrorCode.JWT_INVALID)); // invalid token 401
        RefreshToken savedRefreshToken = refreshTokenRepository.findByRefreshToken(tokenRequest.refreshToken())
                .orElseThrow(() -> new ServiceException(ErrorCode.REFRESH_TOKEN_NOT_EXIST)); // not exist token 404

        if(!jwtUserClaim.userId().equals(savedRefreshToken.getUserId())) // userId 비교
            throw new ServiceException(ErrorCode.USER_NOT_MATCHED);

        refreshTokenRepository.delete(savedRefreshToken); // refresh token

        String accessToken = jwtHandler.createAccessToken(new JwtUserClaim(jwtUserClaim.userId(), jwtUserClaim.role()));
        String refreshToken = jwtHandler.createRefreshToken(new JwtUserClaim(jwtUserClaim.userId(), jwtUserClaim.role()));

        return new TokenResponse(accessToken, refreshToken);
    }
}
