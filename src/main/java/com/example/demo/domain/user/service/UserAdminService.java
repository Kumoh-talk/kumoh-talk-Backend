package com.example.demo.domain.user.service;


import com.example.demo.application.user.dto.request.UpdateUserInfoRequest;
import com.example.demo.application.user.dto.response.UserInfo;
import com.example.demo.domain.user.entity.UserTarget;
import com.example.demo.domain.user.implement.UserReader;
import com.example.demo.domain.user.implement.UserWriter;
import com.example.demo.global.base.dto.page.GlobalPageResponse;

import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.infra.user.entity.User;
import com.example.demo.infra.user.repository.UserJpaRepository;
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

    private final UserReader userReader;
    private final UserService userService;
    private final UserWriter userWriter;

    public Boolean isAdmin(Long userId) {
        UserTarget user = userReader.findUserTarget(userId).orElse(null);
        if (user != null) {
            return userWriter.isAdmin(userId);
        }
        return false;
    }

    public GlobalPageResponse<UserInfo> getAllUsers(Pageable pageable) {
        Page<UserInfo> userInfoPage = userReader.findAllUsers(pageable);
        return GlobalPageResponse.create(userInfoPage);
    }

    @Transactional
    public void updateUserInfo(Long userId, UpdateUserInfoRequest request) {
        UserTarget user = userReader.findUserTarget(userId)
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
        if(userReader.checkNickNameDuplicate(request.nickname())) {
            throw new ServiceException(ErrorCode.EXIST_SAME_NICKNAME);
        }
        userWriter.updateUserInfo(userId, request);
    }

    @Transactional
    public void deleteUser(Long userId) {
        userWriter.deleteUser(userId);
    }
}
