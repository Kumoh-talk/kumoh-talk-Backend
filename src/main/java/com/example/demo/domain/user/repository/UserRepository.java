package com.example.demo.domain.user.repository;

import com.example.demo.application.user.dto.request.UpdateUserInfoRequest;
import com.example.demo.application.user.dto.response.UserInfo;
import com.example.demo.global.jwt.JwtUserClaim;
import com.example.demo.infra.user.entity.User;
import com.example.demo.domain.user.entity.UserTarget;
import com.example.demo.infra.user_additional_info.entity.UserAdditionalInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface UserRepository {

	Optional<User> findUser(Long userId);
	Optional<UserTarget> findUserTarget(Long userId);
	boolean checkNickNameDuplicate(String nickName);
	Page<UserInfo> findAllUsers(Pageable pageable);
	void deleteUser(Long userId);
	void updateNickName(Long userId, String newNickName);
	void changeProfileUrl(Long userId, String profileUrl);
	void setDefaultProfileUrl(Long userId, String defaultImageUrl);
	JwtUserClaim setInitialInfo(String nickname, String name, String imageUrl, Long userId);
	boolean isAdmin(Long userId);
	void updateUserInfo(Long userId, UpdateUserInfoRequest request);
	UserAdditionalInfo getUserAdditionalInfo(Long userId);
}
