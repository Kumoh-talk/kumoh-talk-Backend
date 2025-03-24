package com.example.demo.infra.token.repository;

import com.example.demo.domain.token.entity.RefreshTokenData;
import com.example.demo.domain.token.repository.RefreshTokenRepository;
import com.example.demo.infra.token.entity.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {
    private final RefreshTokenCrudRepository refreshTokenCrudRepository;

    @Override
    public void deleteUserRefreshToken(Long userId) {
        refreshTokenCrudRepository.deleteById(userId);
    }

    @Override
    public void saveRefreshToken(RefreshTokenData refreshTokenData) {
        RefreshToken refreshToken = new RefreshToken(refreshTokenData.getUserId(), refreshTokenData.getRefreshToken());
        refreshTokenCrudRepository.save(refreshToken);
    }

    @Override
    public Optional<RefreshTokenData> findRefreshToken(Long userId) {
        return refreshTokenCrudRepository.findById(userId).map(
                refreshToken -> RefreshTokenData.builder()
                        .userId(refreshToken.getUserId())
                        .refreshToken(refreshToken.getRefreshToken())
                        .build()
        );
    }
}
