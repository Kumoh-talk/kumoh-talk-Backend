package com.example.demo.domain.token.implement;

import com.example.demo.domain.token.entity.RefreshTokenData;
import com.example.demo.domain.token.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TokenReader {
    private final RefreshTokenRepository refreshTokenRepository;

    public Optional<RefreshTokenData> findUserRefreshToken(Long userId){
        return refreshTokenRepository.findRefreshToken(userId);
    }
}
