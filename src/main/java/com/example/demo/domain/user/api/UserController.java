package com.example.demo.domain.user.api;


import static com.example.demo.global.base.dto.ResponseUtil.*;

import com.example.demo.domain.user.application.UserService;
import com.example.demo.domain.user.dto.request.UserPasswordUpdateRequest;
import com.example.demo.domain.user.dto.request.UserUpdateRequest;
import com.example.demo.domain.user.dto.response.UserInfoResponse;
import com.example.demo.domain.user.dto.response.UserUpdateResponse;
import com.example.demo.global.aop.AssignUserId;
import com.example.demo.global.base.dto.ResponseBody;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @AssignUserId
    @GetMapping("/profile")
    public ResponseEntity<ResponseBody<UserInfoResponse>> getUserProfile(Long userId) {

        return ResponseEntity.ok(createSuccessResponse(userService.getUserProfile(userId)));
    }

    @AssignUserId
    @PatchMapping("/profile")
    public ResponseEntity<ResponseBody<UserUpdateResponse>> updateUserProfile(Long userId,
                                                                @RequestBody @Valid UserUpdateRequest request) {

        return ResponseEntity.ok(createSuccessResponse(userService.updateUserProfile(userId, request)));
    }

    @AssignUserId
    @PatchMapping("/profile-password")
    public ResponseEntity<ResponseBody<Void>> updateUserPassword(Long userId,
                                                                 @RequestBody @Valid UserPasswordUpdateRequest request) {
        userService.updateUserPassword(userId, request);
        return ResponseEntity.ok().body(createSuccessResponse());
    }
}
