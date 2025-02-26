package com.example.demo.domain.user.service;

import com.example.demo.application.token.dto.TokenResponse;
import com.example.demo.application.user.dto.request.CompleteRegistrationRequest;
import com.example.demo.application.user.dto.request.UpdateNicknameRequest;
import com.example.demo.application.user.dto.response.UserInfo;
import com.example.demo.application.user.dto.response.UserProfile;
import com.example.demo.domain.token.implement.TokenWriter;
import com.example.demo.domain.user.entity.UserTarget;
import com.example.demo.domain.user.implement.UserReader;
import com.example.demo.domain.user.implement.UserWriter;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.global.jwt.JwtHandler;
import com.example.demo.global.jwt.JwtUserClaim;
import com.example.demo.global.utils.S3UrlUtil;
import com.example.demo.infra.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserReader userReader;
    private final UserWriter userWriter;
    private final TokenWriter tokenWriter;
    private final JwtHandler jwtHandler;
    private final S3UrlUtil s3UrlUtil;

    public void checkNicknameDuplicate(String nickname) {
        if(userReader.checkNickNameDuplicate(nickname)) { // 닉네임 중복 체크
            throw new ServiceException(ErrorCode.EXIST_SAME_NICKNAME);
        }
    }

    @Transactional
    public TokenResponse completeRegistration(Long userId, CompleteRegistrationRequest request) {
        UserTarget user = userReader.findUserTarget(userId)  // 유저가 존재하는지 확인
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
        if(userReader.checkNickNameDuplicate(request.nickname())){ // 닉네임 중복 체크
            throw new ServiceException(ErrorCode.EXIST_SAME_NICKNAME);
        }
        JwtUserClaim claim = userWriter.setInitialInfo(request.nickname(), request.name(), s3UrlUtil.getDefaultImageUrl(), userId);
        return jwtHandler.createTokens(claim);
    }

    public void logout(Long userId) {
        // TODO. blacklist access token?
        tokenWriter.deleteUserRefreshToken(userId);
    }

    public User validateUser(Long userId) {
        return userReader.findUser(userId) // 유저가 존재하는지 확인
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public void updateNickname(Long userId, @Valid UpdateNicknameRequest request) {
        UserTarget user = userReader.findUserTarget(userId) // 유저가 존재하는지 확인
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
        this.checkNicknameDuplicate(request.nickname());
        userWriter.updateNickName(userId, request.nickname());
    }

    public UserInfo getUserInfo(Long userId) {
        return UserInfo.from(this.validateUser(userId));
    }

    public UserProfile getUserProfile(Long userId) {
        return UserProfile.create(this.validateUser(userId));
    }
}
