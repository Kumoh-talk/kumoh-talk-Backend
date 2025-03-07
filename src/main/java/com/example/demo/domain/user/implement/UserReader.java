package com.example.demo.domain.user.implement;

import com.example.demo.domain.base.page.GlobalPageableDto;
import com.example.demo.domain.user.entity.UserInfo;
import com.example.demo.domain.user.entity.UserProfile;
import com.example.demo.domain.user.entity.UserTarget;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.domain.user_addtional_info.entity.UserAdditionalInfoData;
import com.example.demo.infra.user.entity.User;
import com.example.demo.infra.user_additional_info.entity.UserAdditionalInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserReader {
	private final UserRepository userRepository;

	public Optional<UserTarget> findUserTarget(Long userId) {
		return userRepository.findUserTarget(userId);
	}
	// 체크함
	public boolean checkNickNameDuplicate(String nickName) {
		return userRepository.checkNickNameDuplicate(nickName);
	}
	public Page<UserInfo> findAllUsers(GlobalPageableDto globalPageableDto) {
		return userRepository.findAllUsers(globalPageableDto);
	}
	public UserAdditionalInfoData getUserAdditionalInfoData(Long userId) {
		return userRepository.getUserAdditionalInfoData(userId);
	}
	public UserInfo getUserInfo(Long userId) {
		return userRepository.getUserInfo(userId);
	}
	public boolean validateUser(Long userId) {
		return userRepository.existsUserTarget(userId);
	}
	public Optional<UserProfile> getUserProfile(Long userId) {
		return userRepository.getUserProfile(userId);
	}
}
