package com.example.demo.domain.user.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.UserTarget;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
	private final UserJpaRepository userJpaRepository;

	@Override
	public Optional<UserTarget> findUser(Long userId) {
		return userJpaRepository.findById(userId)
			.map(user -> UserTarget.builder()
				.userId(userId)
				.nickName(user.getNickname())
				.userRole(user.getRole())
				.build());
	}
}
