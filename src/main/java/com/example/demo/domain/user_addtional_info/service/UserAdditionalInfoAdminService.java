package com.example.demo.domain.user_addtional_info.service;

import com.example.demo.domain.user.implement.UserReader;
import com.example.demo.domain.user_addtional_info.entity.UserAdditionalInfoData;
import com.example.demo.domain.user_addtional_info.implement.UserAdditionalInfoReader;
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
public class UserAdditionalInfoAdminService {

    private final UserReader userReader;
    private final UserAdditionalInfoReader userAdditionalInfoReader;

    public Page<UserAdditionalInfoData> getAllUserAdditionalInfos(Pageable pageable) {
        return  userAdditionalInfoReader.findAllUserAdditionalInfos(pageable);
    }

    public UserAdditionalInfoData getUserAdditionalInfo(Long userId) {
        userReader.validateUser(userId);
        return userAdditionalInfoReader.getUserAdditionalInfoData(userId)
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_ADDITIONAL_INFO_NOT_FOUND));
    }
}
