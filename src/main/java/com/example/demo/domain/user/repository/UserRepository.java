package com.example.demo.domain.user.repository;

import java.util.Optional;

import org.springframework.security.config.annotation.web.PortMapperDsl;

import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.UserTarget;

public interface UserRepository {

	Optional<UserTarget> findUser(Long userId);
}
