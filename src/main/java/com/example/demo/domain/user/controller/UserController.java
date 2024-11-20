package com.example.demo.domain.user.controller;


import static com.example.demo.global.base.dto.ResponseUtil.*;
import static com.example.demo.global.regex.UserRegex.NICKNAME_REGEXP;

import com.example.demo.domain.token.domain.dto.TokenResponse;
import com.example.demo.domain.user.domain.dto.request.UpdateNicknameRequest;
import com.example.demo.domain.user.domain.dto.response.UserInfo;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.domain.user.domain.dto.request.CompleteRegistrationRequest;
import com.example.demo.global.aop.AssignUserId;
import com.example.demo.global.base.dto.ResponseBody;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    /**
     * GUEST 사용자에 한해서 닉네임 중복 여/부를 확인하는 api
     * TODO. 현재는 중복 체크는 GUEST 유저에게만 허용
     */
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_GUEST')")
    @GetMapping("/check-nickname")
    public ResponseEntity<ResponseBody<Void>> checkNicknameDuplicate(
            @Param("nickname") @Pattern(regexp = NICKNAME_REGEXP, message = "닉네임 정규식을 맞춰주세요.") String nickname) {
        userService.checkNicknameDuplicate(nickname);
        return ResponseEntity.ok(createSuccessResponse());
    }

    /**
     * GUEST 사용자에 한해서 초기 추가정보를 입력받는 api
     */
    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_GUEST')")
    @PatchMapping("/complete-registration")
    public ResponseEntity<ResponseBody<TokenResponse>> completeRegistration(@RequestBody @Valid CompleteRegistrationRequest request,
                                                                            Long userId) {
        return ResponseEntity.ok(createSuccessResponse(userService.completeRegistration(userId, request)));
    }

    /**
     * 로그아웃 api
     * TODO. blacklist 고민
     */
    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    @DeleteMapping("/logout")
    public ResponseEntity<ResponseBody<Void>> logout(Long userId) {
        userService.logout(userId);
        return ResponseEntity.ok(createSuccessResponse());
    }

    /**
     * 사용자 닉네임 수정 api
     */
    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    @PatchMapping("/me/nickname")
    public ResponseEntity<ResponseBody<Void>> updateNickname(@RequestBody @Valid UpdateNicknameRequest request,
                                                                      Long userId) {
        userService.updateNickname(userId, request);
        return ResponseEntity.ok(createSuccessResponse());
    }

    /**
     * 기본 사용자 정보 확인 api
     */
    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    @GetMapping("/me")
    public ResponseEntity<ResponseBody<UserInfo>> getUserInfo(Long userId) {
        return ResponseEntity.ok(createSuccessResponse(userService.getUserInfo(userId)));
    }
}
