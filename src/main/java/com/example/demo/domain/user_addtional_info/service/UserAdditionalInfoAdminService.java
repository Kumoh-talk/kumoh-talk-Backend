package com.example.demo.domain.user_addtional_info.service;

import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.domain.user_addtional_info.domain.dto.response.UserAdditionalInfoResponse;
import com.example.demo.domain.user_addtional_info.repository.UserAdditionalInfoRepository;
import com.example.demo.global.base.dto.page.GlobalPageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserAdditionalInfoAdminService {

    private final UserAdditionalInfoRepository userAdditionalInfoRepository;
    private final UserService userService;
    private final UserAdditionalInfoService userAdditionalInfoService;

    public GlobalPageResponse<UserAdditionalInfoResponse> getAllUserAdditionalInfos(Pageable pageable) {
        Page<UserAdditionalInfoResponse> pages = userAdditionalInfoRepository.findAll(pageable).map(UserAdditionalInfoResponse::from);
        return GlobalPageResponse.create(pages);
    }

    public UserAdditionalInfoResponse getUserAdditionalInfo(Long userId) {
        User user = userService.validateUser(userId);
        userAdditionalInfoService.validateUserAdditionalInfo(user.getUserAdditionalInfo());
        return UserAdditionalInfoResponse.from(user.getUserAdditionalInfo());
    }
}
