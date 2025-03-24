package com.example.demo.infra.token.repository;

import com.example.demo.infra.token.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenCrudRepository extends CrudRepository<RefreshToken, Long> {
}
