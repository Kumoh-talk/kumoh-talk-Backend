package com.example.demo.domain.user_addtional_info.implement;

import com.example.demo.application.user_additional_info.dto.request.CreateUserAdditionalInfoRequest;
import com.example.demo.application.user_additional_info.dto.vo.StudentStatus;
import com.example.demo.domain.user.entity.UserTarget;
import com.example.demo.domain.user_addtional_info.entity.UserAdditionalInfoData;
import com.example.demo.domain.user_addtional_info.repository.UserAdditionalInfoRepository;
import com.example.demo.global.jwt.JwtUserClaim;
import com.example.demo.infra.user_additional_info.entity.UserAdditionalInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAdditionalInfoWriter {
    private final UserAdditionalInfoRepository userAdditionalInfoRepository;

    public JwtUserClaim createUserAdditionalInfo(Long userId, CreateUserAdditionalInfoRequest request) {
        return userAdditionalInfoRepository.createUserAdditionalInfo(userId, request);
    }
    public void updateAcademicInfo(Long userId, int grade, StudentStatus studentStatus) {
        userAdditionalInfoRepository.updateAcademicInfo(userId, grade, studentStatus);
    }
}
