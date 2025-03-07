package com.example.demo.domain.user.service;


import com.example.demo.domain.base.page.GlobalPageableDto;
import com.example.demo.domain.user.entity.UpdateUserInfo;
import com.example.demo.domain.user.entity.UserInfo;
import com.example.demo.domain.user.implement.UserReader;
import com.example.demo.domain.user.implement.UserWriter;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserAdminService {

    private final UserReader userReader;
    private final UserWriter userWriter;

    public Boolean isAdmin(Long userId) {
//        UserTarget user = userReader.findUserTarget(userId).orElse(null);
//        if (user != null) {
//            return userWriter.isAdmin(userId);
//        }
        if(userReader.validateUser(userId)){
            return userWriter.isAdmin(userId);
        }
        return false;
    }

    public GlobalPageableDto getAllUsers(GlobalPageableDto globalPageableDto) {
        Page<UserInfo> userInfoPage = userReader.findAllUsers(globalPageableDto);
        return GlobalPageableDto.create(userInfoPage.getPageable());
    }

    @Transactional
    public void updateUserInfo(Long userId, UpdateUserInfo request) {

        if(!userReader.validateUser(userId)) {
            throw new ServiceException(ErrorCode.USER_NOT_FOUND);
        }
        userReader.checkNickNameDuplicate(request.getNickname());
        userWriter.updateUserInfo(userId, request);
    }

    @Transactional
    public void deleteUser(Long userId) {
        userWriter.deleteUser(userId);
    }
}
