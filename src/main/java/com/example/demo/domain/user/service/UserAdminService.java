package com.example.demo.domain.user.service;


import com.example.demo.domain.user.entity.UpdateUserInfo;
import com.example.demo.domain.user.entity.UserInfo;
import com.example.demo.domain.user.implement.UserReader;
import com.example.demo.domain.user.implement.UserWriter;
import com.example.demo.global.base.dto.page.GlobalPageResponse;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserAdminService {

    private final UserReader userReader;
    private final UserWriter userWriter;

    public Boolean isAdmin(Long userId) {
        if(userReader.isUserExist(userId)){
            return userWriter.isAdmin(userId);
        }
        return false;
    }

    public Page<UserInfo> getAllUsers(Pageable pageable) {
        return userReader.findAllUsers(pageable);
    }

    @Transactional
    public void updateUserInfo(Long userId, UpdateUserInfo request) {

        userReader.validateUser(userId);
        userReader.checkNickNameDuplicate(request.getNickname());
        userWriter.updateUserInfo(userId, request);
    }

    @Transactional
    public void deleteUser(Long userId) {
        userWriter.deleteUser(userId);
    }
}
