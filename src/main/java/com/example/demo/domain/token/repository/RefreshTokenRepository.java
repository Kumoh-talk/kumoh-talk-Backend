package com.example.demo.domain.token.repository;

import com.example.demo.domain.token.entity.RefreshTokenData;

import java.util.Optional;

public interface RefreshTokenRepository{
    void deleteUserRefreshToken(Long userId);
    void saveRefreshToken(RefreshTokenData refreshTokenData);
    Optional<RefreshTokenData> findRefreshToken(Long userId);
}
