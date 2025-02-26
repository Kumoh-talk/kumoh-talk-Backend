package com.example.demo.infra.user_additional_info.repository;

import com.example.demo.application.user_additional_info.dto.request.CreateUserAdditionalInfoRequest;
import com.example.demo.application.user_additional_info.dto.response.UserAdditionalInfoResponse;
import com.example.demo.application.user_additional_info.dto.vo.StudentStatus;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.domain.user_addtional_info.repository.UserAdditionalInfoRepository;
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
public class UserAdditionalInfoRepositoryImpl implements UserAdditionalInfoRepository {

    private final UserAdditionalInfoJpaRepository userAdditionalInfoJpaRepository;
    private final UserRepository userRepository;

    @Override
    public Page<UserAdditionalInfoResponse> findAll(Pageable pageable) {
        return userAdditionalInfoJpaRepository.findAll(pageable).map(UserAdditionalInfoResponse::from);
    }

    @Override
    public JwtUserClaim createUserAdditionalInfo(Long userId, CreateUserAdditionalInfoRequest request) {
        Optional<User> user = userRepository.findUser(userId);
        user.get().mapAdditionalInfo(UserAdditionalInfo.from(request));
        user.get().updateUserRoleToActiveUser(); // 처음 사용자 추가 정보를 작성하면, ACTIVE_USER 로 권한 업데이트
        return JwtUserClaim.create(user.get());
    }

    @Override
    public void updateAcademicInfo(Long userId, int grade, StudentStatus studentStatus) {
        Optional<User> user = userRepository.findUser(userId);
        user.get().getUserAdditionalInfo().updateAcademicInfo(grade, studentStatus);
    }

    @Override
    public Optional<UserAdditionalInfoResponse> getUserAdditionalInfo(Long userId) {
        return userAdditionalInfoJpaRepository.findById(userId).map(UserAdditionalInfoResponse::from);
    }
}
