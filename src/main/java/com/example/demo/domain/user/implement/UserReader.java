package com.example.demo.domain.user.implement;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.demo.domain.user.domain.UserTarget;
import com.example.demo.domain.user.repository.UserJpaRepository;
import com.example.demo.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserReader {
	private final UserRepository userRepository;


	public Optional<UserTarget> findUser(Long userId) {
		return userRepository.findUser(userId);
	}

}
