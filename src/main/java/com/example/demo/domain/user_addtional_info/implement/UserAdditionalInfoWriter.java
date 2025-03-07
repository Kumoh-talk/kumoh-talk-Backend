package com.example.demo.domain.user_addtional_info.implement;

import com.example.demo.application.user_additional_info.dto.request.CreateUserAdditionalInfoRequest;
import com.example.demo.domain.user_addtional_info.entity.UserAdditionalInfoData;
import com.example.demo.domain.user_addtional_info.repository.UserAdditionalInfoRepository;
import com.example.demo.domain.user_addtional_info.vo.StudentStatus;
import com.example.demo.global.jwt.JwtUserClaim;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAdditionalInfoWriter {
    private final UserAdditionalInfoRepository userAdditionalInfoRepository;

    public void createUserAdditionalInfo(Long userId, UserAdditionalInfoData request) {
        userAdditionalInfoRepository.createUserAdditionalInfo(userId, request);
    }
    public void updateAcademicInfo(Long userId, int grade, StudentStatus studentStatus) {
        userAdditionalInfoRepository.updateAcademicInfo(userId, grade, studentStatus);
    }
}
