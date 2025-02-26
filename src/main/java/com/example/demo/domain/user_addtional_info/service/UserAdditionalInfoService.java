package com.example.demo.domain.user_addtional_info.service;


import com.example.demo.application.token.dto.TokenResponse;
import com.example.demo.application.user_additional_info.dto.request.CreateUserAdditionalInfoRequest;
import com.example.demo.application.user_additional_info.dto.request.UpdateUserAcademicInfoRequest;
import com.example.demo.application.user_additional_info.dto.response.UserAdditionalInfoResponse;
import com.example.demo.domain.user.entity.UserTarget;
import com.example.demo.domain.user.implement.UserReader;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.domain.user_addtional_info.entity.UserAdditionalInfoData;
import com.example.demo.domain.user_addtional_info.implement.UserAdditionalInfoReader;
import com.example.demo.domain.user_addtional_info.implement.UserAdditionalInfoWriter;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.global.jwt.JwtHandler;
import com.example.demo.global.jwt.JwtUserClaim;
import com.example.demo.infra.user.entity.User;
import com.example.demo.infra.user_additional_info.entity.UserAdditionalInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.global.base.exception.ErrorCode.USER_ADDITIONAL_INFO_CONFLICT;
import static com.example.demo.global.base.exception.ErrorCode.USER_ADDITIONAL_INFO_NOT_FOUND;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserAdditionalInfoService {

    private final UserReader userReader;
    private final JwtHandler jwtHandler;
    private final UserAdditionalInfoReader userAdditionalInfoReader;
    private final UserAdditionalInfoWriter userAdditionalInfoWriter;

    public UserAdditionalInfoResponse getUserAdditionalInfo(Long userId) {
        UserTarget user = userReader.findUserTarget(userId) // 유저가 존재하는지 확인
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
        UserAdditionalInfoData userAdditionalInfoData = UserAdditionalInfoData.from(userReader.getUserAdditionalInfo(userId)); // UserAdditionalInfoData로 UserAdditionalInfo 받아옴
        this.validateUserAdditionalInfo(userAdditionalInfoData); // userAdditionalInfo가 존재하는지 확인
        return userAdditionalInfoReader.getUserAdditionalInfo(userId).get();
    }

    public void validateUserAdditionalInfo(UserAdditionalInfoData userAdditionalInfoData) {
        if (userAdditionalInfoData == null) {
            throw new ServiceException(USER_ADDITIONAL_INFO_NOT_FOUND);
        }
    }

    @Transactional
    public TokenResponse createUserAdditionalInfo(Long userId, @Valid CreateUserAdditionalInfoRequest request) {
        UserTarget user = userReader.findUserTarget(userId) // 유저가 존재하는지 확인
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
        UserAdditionalInfoData userAdditionalInfoData = UserAdditionalInfoData.from(userReader.getUserAdditionalInfo(userId)); // UserAdditionalInfoData로 UserAdditionalInfo 받아옴
        if(userAdditionalInfoData != null) { // 이전에 입력한 정보가 없을 때, 새로운 정보를 입력
            throw new ServiceException(USER_ADDITIONAL_INFO_CONFLICT);
        }
        JwtUserClaim claim = userAdditionalInfoWriter.createUserAdditionalInfo(userId, request);
        return jwtHandler.createTokens(claim); // 업데이트된 새 토큰 발급
    }

    @Transactional
    public void updateUserAcademicInfo(Long userId, @Valid UpdateUserAcademicInfoRequest request) {
        UserTarget user = userReader.findUserTarget(userId) // 유저가 존재하는지 확인
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
        UserAdditionalInfoData userAdditionalInfoData = UserAdditionalInfoData.from(userReader.getUserAdditionalInfo(userId)); // UserAdditionalInfoData로 UserAdditionalInfo 받아옴
        this.validateUserAdditionalInfo(userAdditionalInfoData); // userAdditionalInfo가 존재하는지 확인
        userAdditionalInfoWriter.updateAcademicInfo(userId, request.grade(), request.studentStatus()); // UserAcademicInfo 업데이트
    }
}
