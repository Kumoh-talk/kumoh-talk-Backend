package com.example.demo.global.auth.token.application;

import com.example.demo.domain.auth.dto.response.LoginResponse;
import com.example.demo.global.auth.token.domain.RefreshToken;
import com.example.demo.global.auth.token.repository.RefreshTokenRepository;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.global.utils.HttpServletUtil;
import io.jsonwebtoken.*;

import com.example.demo.domain.auth.application.CustomUserDetailsService;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.demo.global.base.exception.ErrorCode.NOT_AUTHORIZED_TOKEN;

@Slf4j
@RequiredArgsConstructor
@Component
public class TokenProvider {
    private static final long MILLI_SECOND = 1000L;
    private static final String AUTHORITY = "auth";
    private final CustomUserDetailsService userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final HttpServletUtil httpServletUtil;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.expiry-seconds.access-token}")
    private Long accessTokenExpirySeconds;

    @Value("${jwt.expiry-seconds.refresh-token}")
    private Long refreshTokenExpirySeconds;


    public LoginResponse generateToken(Authentication authentication, HttpServletRequest request) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String accessToken = Jwts.builder()
                .setIssuer(issuer)
                .setSubject(authentication.getName())
                .claim(AUTHORITY, authorities)
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpirySeconds*MILLI_SECOND))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .compact();

        String refreshTokenId = UUID.randomUUID().toString();
        String refreshToken = Jwts.builder()
                .setIssuer(issuer)
                .setId(refreshTokenId)
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpirySeconds*MILLI_SECOND))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .compact();

        refreshTokenRepository.save(RefreshToken.builder()
                .id(refreshTokenId)
                .ip(httpServletUtil.getClientIpAddr(request))
                .authorities(authentication.getAuthorities())
                .refreshToken(refreshToken)
                .build());

        log.info("사용자 IP : " + httpServletUtil.getClientIpAddr(request));

        return new LoginResponse("Bearer", accessToken, refreshTokenId);
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new ServiceException(NOT_AUTHORIZED_TOKEN);
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .toList();

        String username = claims.getSubject();
        UserDetails user = userDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(user, "", authorities);
    }

    public void validateToken(String token) {
        Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token);
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
