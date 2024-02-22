package com.example.demo.global.auth.token.application;

import com.example.demo.global.auth.token.exception.NotSupportedTokenException;
import com.example.demo.global.base.exception.ServiceException;
import io.jsonwebtoken.*;

import com.example.demo.domain.auth.application.CustomUserDetailsService;
import com.example.demo.global.auth.token.exception.ExpiredTokenException;
import com.example.demo.global.auth.token.exception.InvalidTokenException;
import io.jsonwebtoken.security.Keys;
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
import java.util.stream.Collectors;

import static com.example.demo.global.base.exception.ErrorCode.NOT_AUTHORIZED_TOKEN;

@Component
public class TokenProvider {
    private static final long MILLI_SECOND = 1000L;
    private static final String AUTHORITY = "auth";
    private final String issuer;
    private final String secretKey;
    private final long accessTokenExpirySeconds;
    private final CustomUserDetailsService userDetailsService;

    public TokenProvider(@Value("${jwt.issuer}") String issuer,
                         @Value("${jwt.secret-key}") String secretKey,
                         @Value("${jwt.expiry-seconds.access-token}") long accessTokenExpirySeconds,
                         CustomUserDetailsService userDetailsService) {
        this.issuer = issuer;
        this.secretKey = secretKey;
        this.accessTokenExpirySeconds = accessTokenExpirySeconds;
        this.userDetailsService = userDetailsService;
    }

    public String createAccessToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setIssuer(issuer)
                .setSubject(authentication.getName())
                .claim(AUTHORITY, authorities)
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpirySeconds*MILLI_SECOND))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    public Authentication getAuthentication(String accessToken) {
        validateToken(accessToken);

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
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException();
        } catch (UnsupportedJwtException e) {
            throw new NotSupportedTokenException();
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException();
        }
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
