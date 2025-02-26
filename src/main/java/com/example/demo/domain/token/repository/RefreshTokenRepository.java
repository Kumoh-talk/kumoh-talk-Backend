package com.example.demo.domain.token.repository;

import com.example.demo.infra.token.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface RefreshTokenRepository{
    public void deleteUserRefreshToken(Long userId);
    public void save(RefreshToken refreshToken);
}
