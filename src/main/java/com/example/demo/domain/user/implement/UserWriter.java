package com.example.demo.domain.user.implement;

import com.example.demo.domain.user.entity.CompleteRegistration;
import com.example.demo.domain.user.entity.UpdateUserInfo;
import com.example.demo.domain.user.entity.UserTarget;
import com.example.demo.domain.user.repository.UserRepository;
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
    public UserTarget setInitialInfo(Long userId, CompleteRegistration request) {
        return userRepository.setInitialInfo(userId, request);
    }
    public boolean isAdmin(Long userId) {
        return userRepository.isAdmin(userId);
    }
    public void updateUserInfo(Long userId, UpdateUserInfo request) {
        userRepository.updateUserInfo(userId, request);
    }
}
