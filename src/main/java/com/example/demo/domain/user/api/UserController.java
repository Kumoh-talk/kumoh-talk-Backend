package com.example.demo.domain.user.api;


import static com.example.demo.global.base.dto.ResponseUtil.*;

import com.example.demo.domain.user.service.UserService;
import com.example.demo.domain.user.domain.dto.request.CompleteRegistrationRequest;
import com.example.demo.global.aop.AssignUserId;
import com.example.demo.global.base.dto.ResponseBody;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /**
     * GUEST 사용자에 한해서 닉네임 중복 여/부를 확인하는 api
     */
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_GUEST')")
    @GetMapping("/check-nickname")
    public ResponseEntity<ResponseBody<Void>> checkNicknameDuplicate(@Param("nickname") String nickname) {
        userService.checkNicknameDuplicate(nickname);
        return ResponseEntity.ok(createSuccessResponse());
    }

    /**
     * GUEST 사용자에 한해서 초기 추가정보를 입력받는 api
     */
    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_GUEST')")
    @PatchMapping("/complete-registration")
    public ResponseEntity<ResponseBody<Void>> completeRegistration(@RequestBody @Valid CompleteRegistrationRequest request,
                                                                   Long userId) {
        userService.completeRegistration(userId, request);
        return ResponseEntity.ok(createSuccessResponse());
    }
}
