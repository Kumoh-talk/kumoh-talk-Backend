package com.example.demo.global.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.example.demo.domain.user.domain.vo.Role;

import com.example.demo.global.jwt.refresh_token.RefreshToken;
import com.example.demo.global.jwt.refresh_token.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtHandler {

    public static final String USER_ID = "USER_ID";
    public static final String USER_ROLE = "USER_ROLE";
    private static final long MILLI_SECOND = 1000L;

    private final SecretKey secretKey;
    private final JwtProperties jwtProperties;
    private final RefreshTokenRepository refreshTokenRepository;


    public JwtHandler(JwtProperties jwtProperties, RefreshTokenRepository refreshTokenRepository) {
        this.jwtProperties = jwtProperties;
        this.refreshTokenRepository = refreshTokenRepository;
        secretKey = new SecretKeySpec(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String createAccessToken(JwtUserClaim jwtUserClaim) {
        Map<String, Object> tokenClaims = this.createClaims(jwtUserClaim);
        Date now = new Date(System.currentTimeMillis());
        long expiredTime = jwtProperties.getAccessTokenExpireIn();

        return Jwts.builder()
                .claims(tokenClaims)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expiredTime * MILLI_SECOND))
                .signWith(secretKey)
                .compact();
    }

    public String createRefreshToken(JwtUserClaim jwtUserClaim) {
        Map<String, Object> tokenClaims = this.createClaims(jwtUserClaim);
        Date now = new Date(System.currentTimeMillis());
        long expiredTime = jwtProperties.getRefreshTokenExpireIn();

        String refreshToken = Jwts.builder()
                .claims(tokenClaims)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expiredTime * MILLI_SECOND))
                .signWith(secretKey)
                .compact();

        RefreshToken token = new RefreshToken(refreshToken, jwtUserClaim.userId());
        refreshTokenRepository.save(token);

        return refreshToken;
    }


    public Map<String, Object> createClaims(JwtUserClaim jwtUserClaim) {
        return Map.of(
                USER_ID, jwtUserClaim.userId(),
                USER_ROLE, jwtUserClaim.role()
        );
    }

    // exception은 사용하는 곳에서 처리
    public JwtUserClaim parseToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return this.convert(claims);
    }

    public JwtUserClaim convert(Claims claims) {
        return new JwtUserClaim(
                claims.get(USER_ID, Long.class),
                Role.valueOf(claims.get(USER_ROLE, String.class))
        );
    }
}
