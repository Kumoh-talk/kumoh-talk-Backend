package com.example.demo.application.user_additional_info.controller;

import com.example.demo.application.token.dto.TokenResponse;
import com.example.demo.application.user_additional_info.api.UserAdditionalInfoApi;
import com.example.demo.application.user_additional_info.dto.request.CreateUserAdditionalInfoRequest;
import com.example.demo.application.user_additional_info.dto.request.UpdateUserAcademicInfoRequest;
import com.example.demo.application.user_additional_info.dto.response.UserAdditionalInfoResponse;
import com.example.demo.domain.token.entity.Token;
import com.example.demo.domain.user_addtional_info.entity.UpdateUserAcademicInfo;
import com.example.demo.domain.user_addtional_info.entity.UserAdditionalInfoData;
import com.example.demo.domain.user_addtional_info.service.UserAdditionalInfoService;
import com.example.demo.global.aop.AssignUserId;
import com.example.demo.global.base.dto.ResponseBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.global.base.dto.ResponseUtil.createSuccessResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/userAdditionalInfos")
public class UserAdditionalInfoController implements UserAdditionalInfoApi {

    private static final Logger log = LoggerFactory.getLogger(UserAdditionalInfoController.class);
    private final UserAdditionalInfoService userAdditionalInfoService;

    /**
     * 기본 사용자 정보 확인 api
     * 존재하지 않으면 404
     * 존재하면 UserAdditionalInfoResponse
     */
    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    @GetMapping("/me")
    public ResponseEntity<ResponseBody<UserAdditionalInfoResponse>> getUserAdditionalInfo(Long userId) {
        UserAdditionalInfoData userAdditionalInfoData = userAdditionalInfoService.getUserAdditionalInfoData(userId);
        return ResponseEntity.ok(createSuccessResponse(UserAdditionalInfoResponse.toUserAdditionalInfoResponse(userAdditionalInfoData)));
    }

    /**
     * 사용자 추가 정보 생성 api
     * 존재하면 409
     * 존재하지 않으면 첫 생성이므로, 권한 업데이트 이후 새 토큰을 발급
     */
    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    @PostMapping("/me")
    public ResponseEntity<ResponseBody<TokenResponse>> createUserAdditionalInfo(Long userId,
                                                                                @RequestBody @Valid CreateUserAdditionalInfoRequest request) {
        UserAdditionalInfoData userAdditionalInfoData = CreateUserAdditionalInfoRequest.toUserAdditionalInfoData(request);
        Token token = userAdditionalInfoService.createUserAdditionalInfo(userId, userAdditionalInfoData);
        return ResponseEntity.ok(createSuccessResponse(TokenResponse.create(token.getAccessToken(), token.getRefreshToken())));
    }

    /**
     * 매 학기마다 진행되는 학적 정보 변경 API
     * - 재학상태와 학년 변경
     */
    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_ACTIVE_USER')")
    @PatchMapping("/academic-info")
    public ResponseEntity<ResponseBody<Void>> updateUserAcademicInfo(Long userId,
                                                                     @RequestBody @Valid UpdateUserAcademicInfoRequest request) {
        UpdateUserAcademicInfo updateUserAcademicInfo = UpdateUserAcademicInfoRequest.toUpdateUserAcademicInfo(request);
        userAdditionalInfoService.updateUserAcademicInfo(userId, updateUserAcademicInfo);
        return ResponseEntity.ok(createSuccessResponse());
    }
}
