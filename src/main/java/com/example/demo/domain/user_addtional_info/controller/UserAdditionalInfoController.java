package com.example.demo.domain.user_addtional_info.controller;

import com.example.demo.domain.token.domain.dto.TokenResponse;
import com.example.demo.domain.user_addtional_info.domain.dto.request.CreateUserAdditionalInfoRequest;
import com.example.demo.domain.user_addtional_info.domain.dto.response.UserAdditionalInfoResponse;
import com.example.demo.domain.user_addtional_info.service.UserAdditionalInfoService;
import com.example.demo.global.aop.AssignUserId;
import com.example.demo.global.base.dto.ResponseBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.global.base.dto.ResponseUtil.createSuccessResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/userAdditionalInfos")
public class UserAdditionalInfoController {

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
        return ResponseEntity.ok(createSuccessResponse(userAdditionalInfoService.getUserAdditionalInfo(userId)));
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
        return ResponseEntity.ok(createSuccessResponse( userAdditionalInfoService.createUserAdditionalInfo(userId, request)));
    }
}
