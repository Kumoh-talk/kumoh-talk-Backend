package com.example.demo.domain.user.implement;

import java.util.Optional;

import com.example.demo.application.user.dto.response.UserInfo;
import com.example.demo.infra.user.entity.User;
import com.example.demo.domain.user.entity.UserTarget;
import com.example.demo.infra.user_additional_info.entity.UserAdditionalInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.example.demo.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserReader {
	private final UserRepository userRepository;

	public Optional<UserTarget> findUserTarget(Long userId) {
		return userRepository.findUserTarget(userId);
	}

	public boolean checkNickNameDuplicate(String nickName) {

		return userRepository.checkNickNameDuplicate(nickName);
	}

	public Page<UserInfo> findAllUsers(Pageable pageable) {

		return userRepository.findAllUsers(pageable);
	}
	public Optional<User> findUser(Long userId) {
		return userRepository.findUser(userId);
	}
	public UserAdditionalInfo getUserAdditionalInfo(Long userId) {
		return userRepository.getUserAdditionalInfo(userId);
	}

}
