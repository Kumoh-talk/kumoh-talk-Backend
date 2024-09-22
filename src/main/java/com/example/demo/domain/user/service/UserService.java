package com.example.demo.domain.user.service;

import com.example.demo.domain.token.domain.dto.TokenResponse;
import com.example.demo.domain.token.repository.RefreshTokenRepository;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.dto.request.CompleteRegistrationRequest;
import com.example.demo.domain.user.domain.dto.request.UpdateNicknameRequest;
import com.example.demo.domain.user.domain.dto.response.UserInfo;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.global.jwt.JwtHandler;
import com.example.demo.global.jwt.JwtUserClaim;
import com.example.demo.global.utils.S3UrlUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtHandler jwtHandler;
    private final S3UrlUtil s3UrlUtil;

    public void checkNicknameDuplicate(String nickname) {
        if(userRepository.existsByNickname(nickname)){
            throw new ServiceException(ErrorCode.EXIST_SAME_NICKNAME);
        }
    }

    @Transactional
    public TokenResponse completeRegistration(Long userId, CompleteRegistrationRequest request) {
        User user = this.validateUser(userId);
        if(userRepository.existsByNickname(request.nickname())){
            throw new ServiceException(ErrorCode.EXIST_SAME_NICKNAME);
        }
        user.setInitialInfo(request.nickname(), request.name(), s3UrlUtil.bucket);
        return jwtHandler.createTokens(JwtUserClaim.create(user));
    }

    public void logout(Long userId) {
        refreshTokenRepository.deleteById(userId);
        // TODO. blacklist access token?
    }

    public User validateUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public void updateNickname(Long userId, @Valid UpdateNicknameRequest request) {
        User user = this.validateUser(userId);
        this.checkNicknameDuplicate(request.nickname());
        user.updateNickname(request.nickname());
    }

    public UserInfo getUserInfo(Long userId) {
        User user = this.validateUser(userId);
        return UserInfo.from(user);
    }
}
