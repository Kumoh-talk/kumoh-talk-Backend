package com.example.demo.domain.user.service;

import com.example.demo.domain.token.entity.Token;
import com.example.demo.domain.token.implement.TokenWriter;
import com.example.demo.domain.user.entity.CompleteRegistration;
import com.example.demo.domain.user.entity.UserInfo;
import com.example.demo.domain.user.entity.UserProfile;
import com.example.demo.domain.user.entity.UserTarget;
import com.example.demo.domain.user.implement.UserReader;
import com.example.demo.domain.user.implement.UserWriter;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.global.jwt.JwtHandler;
import com.example.demo.global.jwt.JwtUserClaim;
import com.example.demo.infra.user.entity.User;
import com.example.demo.infra.user.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    // TODO: 다른 도메인의 의존성이 해결되면 인프라 레이어 의존성 삭제
    private final UserJpaRepository userJpaRepository;

    private final UserReader userReader;
    private final UserWriter userWriter;
    private final TokenWriter tokenWriter;
    private final JwtHandler jwtHandler;

    public void checkNicknameDuplicate(String nickname) {
        if(userReader.checkNickNameDuplicate(nickname)) { // 닉네임 중복 체크
            throw new ServiceException(ErrorCode.EXIST_SAME_NICKNAME);
        }
    }

    @Transactional
    public Token completeRegistration(Long userId, CompleteRegistration request) {
        this.validateUserExists(userId);
        this.checkNicknameDuplicate(request.getNickname());
        UserTarget userTarget = userWriter.setInitialInfo(userId, request);
        JwtUserClaim jwtUserClaim = JwtUserClaim.create(userTarget.getUserId(), userTarget.getUserRole());
        return jwtHandler.createTokens(jwtUserClaim);
    }

    public void logout(Long userId) {
        // TODO. blacklist access token?
        tokenWriter.deleteUserRefreshToken(userId);
    }

    public void validateUserExists(Long userId) {
        if(!userReader.validateUser(userId)) {
            throw new ServiceException(ErrorCode.USER_NOT_FOUND);
        }
    }

    /**
     * validateUser 메서드는 지원 중단되는 메서드입니다.
     * 사용자 존재 여부를 확인하기 위해서는 UserReader의 validateUser를 사용해야 합니다.
     */
    @Deprecated(forRemoval = true)
    public User validateUser(Long userId) {
        return userJpaRepository.findById(userId).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
    }

    // 이 친구는 일단 보류
    @Transactional
    public void updateNickname(Long userId, String nickname) {
        this.validateUserExists(userId);
        this.checkNicknameDuplicate(nickname);
        userWriter.updateNickName(userId, nickname);
    }

    public UserInfo getUserInfo(Long userId) {
//        return UserInfoResponse.from(this.validateUser(userId));
        this.validateUserExists(userId);
        return userReader.getUserInfo(userId);
    }

    public Optional<UserProfile> getUserProfile(Long userId) {
        // 프로필 정보를 만들어서 리턴
        this.validateUserExists(userId);
        return userReader.getUserProfile(userId);
//        return UserProfileResponse.create(this.validateUser(userId));
    }
}
