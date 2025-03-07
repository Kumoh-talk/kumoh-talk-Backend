package com.example.demo.domain.token.implement;

import com.example.demo.domain.token.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class TokenWriter {
    private final RefreshTokenRepository refreshTokenRepository;
    public void deleteUserRefreshToken(Long userId) {
        refreshTokenRepository.deleteUserRefreshToken(userId);
    }
}
