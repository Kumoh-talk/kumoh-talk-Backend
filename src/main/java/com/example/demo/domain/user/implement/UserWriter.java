package com.example.demo.domain.user.implement;

import com.example.demo.application.user.dto.request.UpdateUserInfoRequest;
import com.example.demo.domain.token.repository.RefreshTokenRepository;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.jwt.JwtUserClaim;
import com.example.demo.infra.user.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserWriter {
    private final UserRepository userRepository;

    public void deleteUser(Long userId) {
        userRepository.deleteUser(userId);
    }
    public void updateNickName(Long userId, String nickname) {
        userRepository.updateNickName(userId, nickname);
    }
    public void changeProfileUrl(Long userId, String profileUrl) {
        userRepository.changeProfileUrl(userId, profileUrl);
    }

    public void setDefaultProfileUrl(Long userId, String defaultImageUrl) {
        userRepository.setDefaultProfileUrl(userId, defaultImageUrl);
    }
    public JwtUserClaim setInitialInfo(String nickname, String name, String imageUrl, Long userId) {
        return userRepository.setInitialInfo(nickname,name,imageUrl,userId);
    }
    public boolean isAdmin(Long userId) {
        return userRepository.isAdmin(userId);
    }
    public void updateUserInfo(Long userId, UpdateUserInfoRequest request) {
        userRepository.updateUserInfo(userId, request);
    }
}
