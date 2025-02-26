package com.example.demo.infra.token.repository;

import com.example.demo.domain.token.repository.RefreshTokenRepository;
import com.example.demo.infra.token.entity.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {
    private final RefreshTokenCrudRepository refreshTokenCrudRepository;

    @Override
    public void deleteUserRefreshToken(Long userId) {
        refreshTokenCrudRepository.deleteById(userId);
    }

    @Override
    public void save(RefreshToken refreshToken) {
        refreshTokenCrudRepository.save(refreshToken);
    }
}
