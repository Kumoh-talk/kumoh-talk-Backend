package com.example.demo.global.auth.token.filter;

import com.example.demo.domain.auth.dto.response.LoginResponse;
import com.example.demo.global.auth.token.application.TokenProvider;
import com.example.demo.global.auth.token.domain.RefreshToken;
import com.example.demo.global.auth.token.repository.RefreshTokenRepository;
import com.example.demo.global.base.dto.ErrorResponse;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.global.utils.CookieUtil;
import com.example.demo.global.utils.HttpServletUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final HttpServletUtil httpServletUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = resolveToken(request);

        log.debug("JwtFilter에서 token({}) 검증을 시작합니다...", token);
        if(token != null) {
            try {
                tokenProvider.validateToken(token);
            } catch (ExpiredJwtException e) {
                log.error("JWT({})가 만료되었습니다. 만료일: {}", token, e.getClaims().getExpiration());
                try{
                    reIssueToken(request, response, token);
                } catch (ServiceException se){
                    log.error("리프레시 토큰 생성에 실패했습니다. 재 로그인이 필요합니다.{} {}", se.getErrorCode(), se.getMessage());
                    return;
                }
            } catch (RuntimeException e) {
                log.error("JWT({})의 유효성(형식, 서명 등)이 올바르지 않습니다.", token);
                writeErrorResponse(response, ErrorCode.INVALID_TOKEN, HttpServletResponse.SC_UNAUTHORIZED);
                return;

            }

            try {
                Authentication authentication = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (RuntimeException e) {
                log.error("JWT({})로부터 인증정보를 만드는데 실패했습니다: {}", token, e.getMessage());
                writeErrorResponse(response, ErrorCode.UNABLE_TO_CREATE_AUTHENTICATION,
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }

        // 인증 정보가 이미 있는 경우, 토큰이 null인 경우, 인증에 성공한 경우
        log.debug("인증 과정을 마쳤으므로 다음 필터로 진행합니다...");
        filterChain.doFilter(request, response);
    }

    private void reIssueToken(HttpServletRequest request, HttpServletResponse response, String token) throws IOException {
        Optional<Cookie> refreshTokenId = CookieUtil.getCookie(request, "refresh");
        if(refreshTokenId.isEmpty()){
            writeErrorResponse(response, ErrorCode.COOKIE_REFRESH_TOKEN_NOT_FOUND, HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findById(refreshTokenId.get().getValue());
        if(refreshToken.isEmpty()){
            writeErrorResponse(response, ErrorCode.REFRESH_TOKEN_NOT_FOUND, HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String currentIpAddress = httpServletUtil.getClientIpAddr(request);
        if(!refreshToken.get().getIp().equals(currentIpAddress)){
            writeErrorResponse(response, ErrorCode.IP_MISMATCH, HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        refreshTokenRepository.deleteById(refreshToken.get().getId());

        Authentication authentication = tokenProvider.getAuthentication(token);
        LoginResponse tokenInfo = tokenProvider.generateToken(authentication, request);

        CookieUtil.addCookie(response, "access", tokenInfo.getAccessToken(), tokenInfo.getAccessTokenExpirationTime());
        CookieUtil.addCookie(response, "refresh", tokenInfo.getRefreshTokenId(), tokenInfo.getRefreshTokenExpirationTime());
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        return isBearerToken(bearerToken) ? extractJwt(bearerToken) : null;
    }

    private boolean isBearerToken(String token) {
        return token != null && token.startsWith(BEARER_PREFIX);
    }

    private String extractJwt(String bearerToken) {
        return bearerToken.substring(BEARER_PREFIX.length());
    }

//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//        return request.getRequestURI().endsWith();
//    }

    private void writeErrorResponse(HttpServletResponse response, ErrorCode errorCode, int statusCode) throws
            IOException {
        String errorResponseJsonFormat = getErrorResponseJsonFormat(errorCode);
        writeToHttpServletResponse(response, statusCode, errorResponseJsonFormat);
    }

    private String getErrorResponseJsonFormat(ErrorCode errorCode) throws JsonProcessingException {
        ErrorResponse errorResponse = new ErrorResponse(errorCode);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(errorResponse);
    }

    private void writeToHttpServletResponse(HttpServletResponse response, int statusCode, String errorMessage) throws
            IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(errorMessage);
        response.getWriter().flush();
        response.getWriter().close();
    }


}
