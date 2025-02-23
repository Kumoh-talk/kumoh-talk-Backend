package com.example.demo.domain.user.service;

import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.dto.request.UpdateUserInfoRequest;
import com.example.demo.domain.user.domain.dto.response.UserInfo;
import com.example.demo.domain.user.repository.UserJpaRepository;
import com.example.demo.global.base.dto.page.GlobalPageResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserAdminService {

    private final UserJpaRepository userJpaRepository;
    private final UserService userService;

    public Boolean isAdmin(Long userId) {
        Optional<User> userOptional = userJpaRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.isAdmin();
        }

        return false;
    }

    public GlobalPageResponse<UserInfo> getAllUsers(Pageable pageable) {
        Page<UserInfo> userInfoPage = userJpaRepository.findAll(pageable).map(UserInfo::from);
        return GlobalPageResponse.create(userInfoPage);
    }

    @Transactional
    public void updateUserInfo(Long userId, UpdateUserInfoRequest request) {
        User user = userService.validateUser(userId);
        userService.checkNicknameDuplicate(request.nickname());
        user.updateUserInfo(request);
    }

    @Transactional
    public void deleteUser(Long userId) {
        userJpaRepository.deleteById(userId);
    }
}
