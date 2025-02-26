package com.example.demo.infra.user.repository;

import com.example.demo.global.oauth.user.OAuth2Provider;
import com.example.demo.infra.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {
    Optional<User> findByProviderAndProviderId(OAuth2Provider provider, String providerId);

    boolean existsByNickname(String nickname);
}
