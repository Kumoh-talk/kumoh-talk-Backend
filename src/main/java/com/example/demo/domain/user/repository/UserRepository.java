package com.example.demo.domain.user.repository;

import com.example.demo.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUserId(String userId);

    Optional<User> findByUserId(String userId);

    Optional<User> findByNickname(String nickname);

    boolean existsByNickname(String nickname);
}
