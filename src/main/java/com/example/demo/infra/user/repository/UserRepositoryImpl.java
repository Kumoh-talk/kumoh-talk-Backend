package com.example.demo.infra.user.repository;

import com.example.demo.application.user.dto.request.UpdateUserInfoRequest;
import com.example.demo.application.user.dto.response.UserInfo;
import com.example.demo.application.user.dto.vo.Role;
import com.example.demo.domain.user.entity.UserTarget;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.jwt.JwtUserClaim;
import com.example.demo.infra.user.entity.User;
import com.example.demo.infra.user_additional_info.entity.UserAdditionalInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
	private final UserJpaRepository userJpaRepository;

	@Override
	public Optional<User> findUser(Long userId) {
		return userJpaRepository.findById(userId);
	}

	@Override
	public Optional<UserTarget> findUserTarget(Long userId) {
		return userJpaRepository.findById(userId)
			.map(user -> UserTarget.builder()
				.userId(userId)
				.nickName(user.getNickname())
				.userRole(user.getRole())
				.build());
	}

	@Override
	public boolean checkNickNameDuplicate(String nickname){
		return userJpaRepository.existsByNickname(nickname);
	}

	@Override
	public Page<UserInfo> findAllUsers(Pageable pageable) {
		return userJpaRepository.findAll(pageable).map(UserInfo::from);
	}

	@Override
	public void deleteUser(Long userId) {
		userJpaRepository.deleteById(userId);
	}

	@Override
	public void updateNickName(Long userId, String newNickName) {
		Optional<User> user = userJpaRepository.findById(userId);
		user.get().updateNickname(newNickName);
	}

	@Override
	public void changeProfileUrl(Long userId, String profileUrl) {
		Optional<User> user = userJpaRepository.findById(userId);
		user.get().changeProfileUrl(profileUrl);
	}

	@Override
	public void setDefaultProfileUrl(Long userId, String defaultImageUrl) {
		Optional<User> user = userJpaRepository.findById(userId);
		user.get().setDefaultProfileUrl(defaultImageUrl);
	}

	@Override
	public JwtUserClaim setInitialInfo(String nickname, String name, String imageUrl, Long userId) {
		Optional<User> user = userJpaRepository.findById(userId);
		user.get().setInitialInfo(nickname, name, imageUrl);
		return JwtUserClaim.create(user.get());
	}

	@Override
	public boolean isAdmin(Long userId) {
		Optional<User> user = userJpaRepository.findById(userId);
		return Role.ROLE_ADMIN.equals(user.get().getRole());
	}

	@Override
	public void updateUserInfo(Long userId, UpdateUserInfoRequest request) {
		Optional<User> user = userJpaRepository.findById(userId);
		user.get().updateUserInfo(request);
	}

	@Override
	public UserAdditionalInfo getUserAdditionalInfo(Long userId) {
		Optional<User> user = userJpaRepository.findById(userId);
		return user.get().getUserAdditionalInfo();
	}

}
