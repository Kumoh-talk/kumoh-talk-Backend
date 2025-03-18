package com.example.demo.domain.user_addtional_info.service;



import com.example.demo.domain.token.entity.Token;
import com.example.demo.domain.user.implement.UserReader;
import com.example.demo.domain.user.vo.Role;
import com.example.demo.domain.user_addtional_info.entity.UpdateUserAcademicInfo;
import com.example.demo.domain.user_addtional_info.entity.UserAdditionalInfoData;
import com.example.demo.domain.user_addtional_info.implement.UserAdditionalInfoReader;
import com.example.demo.domain.user_addtional_info.implement.UserAdditionalInfoWriter;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.global.jwt.JwtHandler;
import com.example.demo.global.jwt.JwtUserClaim;
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

    public UserAdditionalInfoData getUserAdditionalInfoData(Long userId) {
        userReader.validateUser(userId);
        return userAdditionalInfoReader.getUserAdditionalInfoData(userId)
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_ADDITIONAL_INFO_NOT_FOUND));
    }

    @Transactional
    public Token createUserAdditionalInfo(Long userId, UserAdditionalInfoData request) {
        userReader.validateUser(userId);
        userAdditionalInfoReader.getUserAdditionalInfoData(userId) // 정보가 존재하면 예외 던짐
                .ifPresent(existingInfo -> {
                    throw new ServiceException(ErrorCode.USER_ADDITIONAL_INFO_CONFLICT);
                });
        userAdditionalInfoWriter.createUserAdditionalInfo(userId, request);
        JwtUserClaim claim = new JwtUserClaim(userId, Role.ROLE_ACTIVE_USER); // ROLE_ACTIVE_USER로 변경
        return jwtHandler.createTokens(claim); // 새로운 토큰 발급
    }

    @Transactional
    public void updateUserAcademicInfo(Long userId, UpdateUserAcademicInfo request) {
        userReader.validateUser(userId);
        UserAdditionalInfoData userAdditionalInfoData = userAdditionalInfoReader.getUserAdditionalInfoData(userId)
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_ADDITIONAL_INFO_NOT_FOUND));
        userAdditionalInfoWriter.updateAcademicInfo(userId, request.getGrade(), request.getStudentStatus());
    }
}
