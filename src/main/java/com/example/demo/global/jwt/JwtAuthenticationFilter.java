package com.example.demo.global.jwt;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.global.jwt.exception.JwtAuthenticationException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";
    private final AuthenticationManager authenticationManager;
    private final RequestMatcher requestMatcher = new RequestHeaderRequestMatcher(HttpHeaders.AUTHORIZATION);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!requestMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String tokenValue = resolveToken(request).orElseThrow(() -> new ServiceException(ErrorCode.JWT_NOT_EXIST));
            JwtAuthenticationToken token = new JwtAuthenticationToken(tokenValue); // 인증되지 않은 토큰
            Authentication authentication = this.authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (JwtAuthenticationException e) {
            this.handleServiceException(response, e);
        }
    }

    private Optional<String> resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)){
            return Optional.of(bearerToken.substring(BEARER_PREFIX.length()));
        }
        return Optional.empty();
    }

    private void handleServiceException(HttpServletResponse response, JwtAuthenticationException e) throws IOException {
        response.setStatus(e.getErrorCode().getStatus().value());
        response.setContentType("application/json;charset=UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        String errorResponse = objectMapper.writeValueAsString(new ServiceException(e.getErrorCode()));
        response.getWriter().write(errorResponse);
        response.flushBuffer(); // 커밋
        response.getWriter().close();
    }

}
