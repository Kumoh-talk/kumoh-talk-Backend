package com.example.demo.domain.token.service;

import com.example.demo.domain.token.entity.RefreshTokenData;
import com.example.demo.domain.token.entity.Token;
import com.example.demo.domain.token.implement.TokenReader;
import com.example.demo.domain.token.implement.TokenWriter;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.global.jwt.JwtHandler;
import com.example.demo.global.jwt.JwtUserClaim;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TokenService {

    private final JwtHandler jwtHandler;
    private final TokenWriter tokenWriter;
    private final TokenReader tokenReader;

    public Token refresh(Token token) {
        JwtUserClaim jwtUserClaim = jwtHandler.getClaims(token.getAccessToken())
                .orElseThrow(() -> new ServiceException(ErrorCode.JWT_INVALID)); // invalid token 401

        RefreshTokenData savedRefreshToken = tokenReader.findUserRefreshToken(jwtUserClaim.userId())
                .orElseThrow(() -> new ServiceException(ErrorCode.REFRESH_TOKEN_NOT_EXIST)); // not exist token 404

        if(!token.getRefreshToken().equals(savedRefreshToken.getRefreshToken())) // userId 비교
            throw new ServiceException(ErrorCode.TOKEN_NOT_MATCHED);

        tokenWriter.deleteUserRefreshToken(savedRefreshToken.getUserId());

        return jwtHandler.createTokens(jwtUserClaim);
    }
}
