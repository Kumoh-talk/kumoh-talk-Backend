package com.example.demo.domain.user_addtional_info.service;

import com.example.demo.application.user_additional_info.dto.response.UserAdditionalInfoResponse;
import com.example.demo.domain.user.entity.UserTarget;
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

    public GlobalPageResponse<UserAdditionalInfoData> getAllUserAdditionalInfos(Pageable pageable) {
        Page<UserAdditionalInfoData> pages = userAdditionalInfoReader.findAllUserAdditionalInfos(pageable);
        return GlobalPageResponse.create(pages);
    }

    public UserAdditionalInfoData getUserAdditionalInfo(Long userId) {
        if(!userReader.validateUser(userId)){
            throw new ServiceException(ErrorCode.USER_NOT_FOUND);
        }
        UserAdditionalInfoData userAdditionalInfoData = userReader.getUserAdditionalInfoData(userId); // UserAdditionalInfoData로 UserAdditionalInfo 받아옴
        userAdditionalInfoReader.checkValidateUserAdditionalInfo(userAdditionalInfoData); // userAdditionalInfo가 존재하는지 확인
        return userAdditionalInfoReader.getUserAdditionalInfoData(userId);

    }
}
