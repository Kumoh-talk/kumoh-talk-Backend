package com.example.demo.domain.user_addtional_info.service;

import com.example.demo.application.user_additional_info.dto.response.UserAdditionalInfoResponse;
import com.example.demo.domain.user.entity.UserTarget;
import com.example.demo.domain.user.implement.UserReader;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.domain.user_addtional_info.entity.UserAdditionalInfoData;
import com.example.demo.domain.user_addtional_info.implement.UserAdditionalInfoReader;
import com.example.demo.domain.user_addtional_info.repository.UserAdditionalInfoRepository;
import com.example.demo.global.base.dto.page.GlobalPageResponse;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.infra.user.entity.User;
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

    public GlobalPageResponse<UserAdditionalInfoResponse> getAllUserAdditionalInfos(Pageable pageable) {
        Page<UserAdditionalInfoResponse> pages = userAdditionalInfoReader.findAllUserAdditionalInfos(pageable);
        return GlobalPageResponse.create(pages);
    }

    public UserAdditionalInfoResponse getUserAdditionalInfo(Long userId) {
        UserTarget user = userReader.findUserTarget(userId) // 유저가 존재하는지 확인
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
        UserAdditionalInfoData userAdditionalInfoData = UserAdditionalInfoData.from(userReader.getUserAdditionalInfo(userId)); // UserAdditionalInfoData로 UserAdditionalInfo 받아옴
        userAdditionalInfoReader.checkValidateUserAdditionalInfo(userAdditionalInfoData); // userAdditionalInfo가 존재하는지 확인
        return userAdditionalInfoReader.getUserAdditionalInfo(userId).get();

    }
}
