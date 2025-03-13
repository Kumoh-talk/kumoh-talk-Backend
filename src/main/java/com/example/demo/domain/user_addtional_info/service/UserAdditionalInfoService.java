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
        if(!userReader.validateUser(userId)){
            throw new ServiceException(ErrorCode.USER_NOT_FOUND);
        }
        UserAdditionalInfoData userAdditionalInfoData = userAdditionalInfoReader.getUserAdditionalInfoData(userId);
        this.validateUserAdditionalInfo(userAdditionalInfoData); // userAdditionalInfo가 존재하는지 확인
        return userAdditionalInfoData;
    }

    public void validateUserAdditionalInfo(UserAdditionalInfoData userAdditionalInfoData) {
        if (userAdditionalInfoData == null) {
            throw new ServiceException(USER_ADDITIONAL_INFO_NOT_FOUND);
        }
    }

    @Transactional
    public Token createUserAdditionalInfo(Long userId, UserAdditionalInfoData request) {
        if(!userReader.validateUser(userId)){
            throw new ServiceException(ErrorCode.USER_NOT_FOUND);
        }
        UserAdditionalInfoData userAdditionalInfoData = userAdditionalInfoReader.getUserAdditionalInfoData(userId);
        if (userAdditionalInfoData != null) { // 정보가 존재하지 않을 때만 수행
            throw new ServiceException(USER_ADDITIONAL_INFO_CONFLICT);
        }
        userAdditionalInfoWriter.createUserAdditionalInfo(userId, request);
        JwtUserClaim claim = new JwtUserClaim(userId, Role.ROLE_ACTIVE_USER); // 성공적으로 추가 정보를 입력했다면 role은 ACTIVE_USER로 승격
        return jwtHandler.createTokens(claim); // 업데이트된 새 토큰 발급
    }

    @Transactional
    public void updateUserAcademicInfo(Long userId, UpdateUserAcademicInfo request) {
        if(!userReader.validateUser(userId)){
            throw new ServiceException(ErrorCode.USER_NOT_FOUND);
        }
        UserAdditionalInfoData userAdditionalInfoData = userAdditionalInfoReader.getUserAdditionalInfoData(userId);
        this.validateUserAdditionalInfo(userAdditionalInfoData); // userAdditionalInfo가 존재하는지 확인
        userAdditionalInfoWriter.updateAcademicInfo(userId, request.getGrade(), request.getStudentStatus()); // UserAcademicInfo 업데이트
    }
}
