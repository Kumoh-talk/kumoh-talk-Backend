package com.example.demo.infra.user.repository;

import com.example.demo.domain.user.entity.*;
import com.example.demo.domain.base.page.GlobalPageableDto;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.domain.user_addtional_info.entity.UserAdditionalInfoData;
import com.example.demo.global.utils.S3UrlUtil;
import com.example.demo.infra.user.entity.User;
import com.example.demo.infra.user_additional_info.entity.UserAdditionalInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
	private final UserJpaRepository userJpaRepository;
	private final S3UrlUtil s3UrlUtil;

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
	public boolean existsUserTarget(Long userId) {
		return userJpaRepository.existsById(userId);
	}

	@Override
	public boolean checkNickNameDuplicate(String nickname){
		return userJpaRepository.existsByNickname(nickname);
	}

	@Override
	public Page<UserInfo> findAllUsers(GlobalPageableDto globalPageableDto) {
		return userJpaRepository.findAll(globalPageableDto.getPageable())
				.map(user -> UserInfo.builder()
						.id(user.getId())
						.provider(user.getProvider())
						.nickname(user.getNickname())
						.name(user.getName())
						.profileImageUrl(user.getProfileImageUrl())
						.role(user.getRole())
						.createdAt(user.getCreatedAt())
						.updatedAt(user.getUpdatedAt())
						.build());
	}

	@Override
	public void deleteUser(Long userId) {
		userJpaRepository.deleteById(userId);
	}

	@Override
	public void updateNickName(Long userId, String newNickName) {
		userJpaRepository.updateNickName(userId, newNickName);
	}

	@Override
	public void changeProfileUrl(Long userId, String profileUrl) {
		userJpaRepository.changeProfileUrl(userId, profileUrl);
	}

	@Override
	public void setDefaultProfileUrl(Long userId, String defaultImageUrl) {
		// 기본 이미지 url로 변경
		userJpaRepository.changeProfileUrl(userId, defaultImageUrl);
	}

	@Override
	public UserTarget setInitialInfo(Long userId, CompleteRegistration request) {
		Optional<User> user = userJpaRepository.findById(userId);
		user.get().setInitialInfo(request.getNickname(), request.getName(), s3UrlUtil.getDefaultImageUrl());
		return user.map(userTarget -> UserTarget.builder()
				.userId(userTarget.getId())
				.nickName(userTarget.getNickname())
				.userRole(userTarget.getRole())
				.build()).get();
	}

	// TODO : 수정해야 하는지 확인하기
	@Override
	public boolean isAdmin(Long userId) {
		Optional<User> user = userJpaRepository.findById(userId);
		return user.get().isAdmin();
	}

	@Override
	public void updateUserInfo(Long userId, UpdateUserInfo request) {
		userJpaRepository.updateUserInfo(userId,
				request.getNickname(),
				request.getName(),
				request.getProfileImageUrl(),
				request.getRole()
		);
	}

	// TODO : 수정해야 하는지 확인하기
	@Override
	public UserAdditionalInfoData getUserAdditionalInfoData(Long userId) {
		Optional<User> user = userJpaRepository.findById(userId);
		return UserAdditionalInfo.toUserAdditionalInfoData(user.get().getUserAdditionalInfo());
	}

	// TODO : 수정해야 하는지 확인하기
	@Override
	public UserInfo getUserInfo(Long userId) {
		Optional<User> user = userJpaRepository.findById(userId);
		return user.get().toUserInfo(user.get());
	}

	// TODO : 수정해야 하는지 확인하기
	@Override
	public Optional<UserProfile> getUserProfile(Long userId) {
		Optional<User> user = userJpaRepository.findById(userId);
		return user.map(profile -> UserProfile.builder()
				.name(profile.getName())
				.nickname(profile.getNickname())
				.profileImageUrl(profile.getProfileImageUrl())
				.studentStatus(profile.getUserAdditionalInfo().getStudentStatus())
				.department(profile.getUserAdditionalInfo().getDepartment())
				.grade(profile.getUserAdditionalInfo().getGrade())
				.studentId(profile.getUserAdditionalInfo().getStudentId())
				.createdAt(profile.getCreatedAt())
				.updatedAt(profile.getUpdatedAt())
				.build());
	}

}
