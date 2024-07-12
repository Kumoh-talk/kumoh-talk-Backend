package com.example.demo.domain.user.repository;

import com.example.demo.domain.user.domain.User;
import com.example.demo.global.oauth.user.OAuth2Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByProviderAndProviderId(OAuth2Provider provider, String providerId);

    Optional<User> findByNickname(String nickname);

    boolean existsByNickname(String nickname);
}
