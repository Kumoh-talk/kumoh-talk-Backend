package com.example.demo.infra.user_additional_info.repository;
import com.example.demo.domain.user_addtional_info.entity.UserAdditionalInfoData;
import com.example.demo.domain.user_addtional_info.repository.UserAdditionalInfoRepository;
import com.example.demo.domain.user_addtional_info.vo.StudentStatus;
import com.example.demo.infra.user.entity.User;
import com.example.demo.infra.user.repository.UserJpaRepository;
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
    private final UserJpaRepository userJpaRepository;

    @Override
    public Page<UserAdditionalInfoData> findAll(Pageable pageable) {
        return userAdditionalInfoJpaRepository.findAll(pageable).map(data -> UserAdditionalInfoData.builder()
                .id(data.getId())
                .email(data.getEmail())
                .department(data.getDepartment())
                .studentStatus(data.getStudentStatus())
                .grade(data.getGrade())
                .studentStatus(data.getStudentStatus())
                .phoneNumber(data.getPhoneNumber())
                .isUpdated(data.isUpdated())
                .createdAt(data.getCreatedAt())
                .updatedAt(data.getUpdatedAt())
                .build());
    }

    @Override
    public void createUserAdditionalInfo(Long userId, UserAdditionalInfoData request) {
        User user = userJpaRepository.findById(userId).get();
        user.mapAdditionalInfo(UserAdditionalInfo.toUserAdditionalInfo(request));
        user.updateUserRoleToActiveUser(); // 처음 사용자 추가 정보를 작성하면, ACTIVE_USER 로 권한 업데이트
    }

    @Override
    public void updateAcademicInfo(Long userId, int grade, StudentStatus studentStatus) {
        Optional<User> user = userJpaRepository.findById(userId);
        user.get().getUserAdditionalInfo().updateAcademicInfo(grade, studentStatus);
    }

    @Override
    public Optional<UserAdditionalInfoData> getUserAdditionalInfoData(Long userId) {
        return userJpaRepository.findById(userId)
                .map(user -> Optional.ofNullable(user.getUserAdditionalInfo()))
                .flatMap(userInfo -> userInfo.map(UserAdditionalInfo::toUserAdditionalInfoData));
    }
}
