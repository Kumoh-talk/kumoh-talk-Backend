package com.example.demo.domain.user.repository;

import com.example.demo.domain.user.entity.*;
import com.example.demo.domain.user_addtional_info.entity.UserAdditionalInfoData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface UserRepository {

	Optional<UserTarget> findUserTarget(Long userId);
	boolean existsUserTarget(Long userId);
	boolean checkNickNameDuplicate(String nickName);
	Page<UserInfo> findAllUsers(Pageable pageable);
	void deleteUser(Long userId);
	void updateNickName(Long userId, String newNickName);
	void changeProfileUrl(Long userId, String profileUrl);
	void setDefaultProfileUrl(Long userId, String defaultImageUrl);
	UserTarget setInitialInfo(Long userId, CompleteRegistration request);
	boolean isAdmin(Long userId);
	void updateUserInfo(Long userId, UpdateUserInfo request);
	UserInfo getUserInfo(Long userId);
	Optional<UserProfile> getUserProfile(Long userId);
}
