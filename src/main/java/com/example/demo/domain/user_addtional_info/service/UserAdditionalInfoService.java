package com.example.demo.domain.user_addtional_info.service;

import com.example.demo.domain.token.domain.dto.TokenResponse;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.domain.user_addtional_info.domain.UserAdditionalInfo;
import com.example.demo.domain.user_addtional_info.domain.dto.request.CreateUserAdditionalInfoRequest;
import com.example.demo.domain.user_addtional_info.domain.dto.request.UpdateUserAcademicInfoRequest;
import com.example.demo.domain.user_addtional_info.domain.dto.response.UserAdditionalInfoResponse;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.global.jwt.JwtHandler;
import com.example.demo.global.jwt.JwtUserClaim;
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

    private final UserService userService;
    private final JwtHandler jwtHandler;

    public UserAdditionalInfoResponse getUserAdditionalInfo(Long userId) {
        User user = userService.validateUser(userId);
        this.validateUserAdditionalInfo(user.getUserAdditionalInfo());
        return UserAdditionalInfoResponse.from(user.getUserAdditionalInfo());
    }

    public void validateUserAdditionalInfo(UserAdditionalInfo userAdditionalInfo) {
        if (userAdditionalInfo == null) {
            throw new ServiceException(USER_ADDITIONAL_INFO_NOT_FOUND);
        }
    }

    @Transactional
    public TokenResponse createUserAdditionalInfo(Long userId, @Valid CreateUserAdditionalInfoRequest request) {
        User user = userService.validateUser(userId);
        if (user.getUserAdditionalInfo() != null) {
            throw new ServiceException(USER_ADDITIONAL_INFO_CONFLICT);
        }
        user.mapAdditionalInfo(UserAdditionalInfo.from(request));
        user.updateUserRoleToActiveUser(); // 처음 사용자 추가 정보를 작성하면, ACTIVE_USER 로 권한 업데이트
        return jwtHandler.createTokens(JwtUserClaim.create(user)); // 업데이트된 새 토큰 발급
    }

    @Transactional
    public void updateUserAcademicInfo(Long userId, @Valid UpdateUserAcademicInfoRequest request) {
        User user = userService.validateUser(userId);
        this.validateUserAdditionalInfo(user.getUserAdditionalInfo());
        user.getUserAdditionalInfo().updateAcademicInfo(request.grade(), request.studentStatus());
    }
}
