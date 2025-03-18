package com.example.demo.domain.user.implement;

import com.example.demo.domain.user.entity.UserInfo;
import com.example.demo.domain.user.entity.UserProfile;
import com.example.demo.domain.user.entity.UserTarget;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.domain.user_addtional_info.entity.UserAdditionalInfoData;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

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
	public UserInfo getUserInfo(Long userId) {
		return userRepository.getUserInfo(userId);
	}
	public void validateUser(Long userId) {
		if(!userRepository.existsUserTarget(userId)){
			throw new ServiceException(ErrorCode.USER_NOT_FOUND);
		}
	}
	public boolean isUserExist(Long userId) {
		return userRepository.existsUserTarget(userId);
	}
	public Optional<UserProfile> getUserProfile(Long userId) {
		return userRepository.getUserProfile(userId);
	}
}
