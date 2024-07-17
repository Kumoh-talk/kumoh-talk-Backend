package com.example.demo.domain.user.api;


import static com.example.demo.global.base.dto.ResponseUtil.*;

import com.example.demo.domain.user.application.UserService;
import com.example.demo.domain.user.dto.request.CompleteRegistrationRequest;
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

    /**
     * GUEST 사용자에 한해서 초기 추가정보를 입력받는 api
     */
    @AssignUserId
    @PatchMapping("/complete-registration")
    public ResponseEntity<ResponseBody<Void>> completeRegistration(@RequestBody @Valid CompleteRegistrationRequest request,
                                                                   Long userId) {
        userService.completeRegistration(userId, request);
        return ResponseEntity.ok(createSuccessResponse());
    }

//    @AssignUserId
//    @PatchMapping("/profile")
//    public ResponseEntity<ResponseBody<Void>> updateUserProfile(@RequestBody @Valid UserUpdateRequest request,
//                                                                              Long userId) {
//        userService.updateUserProfile(userId, request);
//        return ResponseEntity.ok(createSuccessResponse());
//    }


}
