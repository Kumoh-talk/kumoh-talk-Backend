package com.example.demo.global.auth.token.repository;

import com.example.demo.global.auth.token.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
