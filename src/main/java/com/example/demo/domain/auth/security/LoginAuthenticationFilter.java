package com.example.demo.domain.auth.security;

import com.example.demo.domain.auth.dto.request.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

public class LoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public LoginAuthenticationFilter() {
        super(new AntPathRequestMatcher("/api/auth/login"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        // 해당 요청이 POST 인지 확인
        if(!isPost(request)) {
            throw new IllegalStateException("Authentication is not supported");
        }

        LoginRequest loginRequest = objectMapper.readValue(request.getReader(), LoginRequest.class);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );

        return this.getAuthenticationManager().authenticate(token);
    }

    private boolean isPost(HttpServletRequest request) {
        return "POST".equals(request.getMethod());
    }
}
