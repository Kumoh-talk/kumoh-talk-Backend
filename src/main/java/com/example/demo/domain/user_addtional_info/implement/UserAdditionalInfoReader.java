package com.example.demo.domain.user_addtional_info.implement;

import com.example.demo.application.user_additional_info.dto.response.UserAdditionalInfoResponse;
import com.example.demo.domain.user_addtional_info.entity.UserAdditionalInfoData;
import com.example.demo.domain.user_addtional_info.repository.UserAdditionalInfoRepository;
import com.example.demo.global.base.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.example.demo.global.base.exception.ErrorCode.USER_ADDITIONAL_INFO_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class UserAdditionalInfoReader {
    private final UserAdditionalInfoRepository userAdditionalInfoRepository;

    public Page<UserAdditionalInfoData> findAllUserAdditionalInfos(Pageable pageable) {
        return userAdditionalInfoRepository.findAll(pageable);
    }

    public void checkValidateUserAdditionalInfo(UserAdditionalInfoData userAdditionalInfoData) {
        if (userAdditionalInfoData == null) {
            throw new ServiceException(USER_ADDITIONAL_INFO_NOT_FOUND);
        }
    }
    public UserAdditionalInfoData getUserAdditionalInfoData(Long userId) {
        return userAdditionalInfoRepository.getUserAdditionalInfoData(userId);
    }
}
