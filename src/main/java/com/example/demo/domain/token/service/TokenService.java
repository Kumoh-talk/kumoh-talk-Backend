package com.example.demo.domain.token.service;

import com.example.demo.application.token.dto.TokenRequest;
import com.example.demo.application.token.dto.TokenResponse;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.global.jwt.JwtHandler;
import com.example.demo.global.jwt.JwtUserClaim;
import com.example.demo.domain.token.repository.RefreshTokenRepository;
import com.example.demo.infra.token.entity.RefreshToken;
import com.example.demo.infra.token.repository.RefreshTokenCrudRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TokenService {

    private final JwtHandler jwtHandler;
    private final RefreshTokenCrudRepository refreshTokenCrudRepository;

    public TokenResponse refresh(TokenRequest tokenRequest) {
        JwtUserClaim jwtUserClaim = jwtHandler.getClaims(tokenRequest.accessToken())
                .orElseThrow(() -> new ServiceException(ErrorCode.JWT_INVALID)); // invalid token 401
        RefreshToken savedRefreshToken = refreshTokenCrudRepository.findById(jwtUserClaim.userId())
                .orElseThrow(() -> new ServiceException(ErrorCode.REFRESH_TOKEN_NOT_EXIST)); // not exist token 404

        if(!tokenRequest.refreshToken().equals(savedRefreshToken.getRefreshToken())) // userId 비교
            throw new ServiceException(ErrorCode.TOKEN_NOT_MATCHED);

        refreshTokenCrudRepository.deleteById(savedRefreshToken.getUserId()); // refresh token

        return jwtHandler.createTokens(jwtUserClaim);
    }
}
