package com.example.demo.domain.user.api;


import com.example.demo.domain.auth.domain.UserPrincipal;
import com.example.demo.domain.user.application.UserService;
import com.example.demo.domain.user.dto.request.UserPasswordUpdateRequest;
import com.example.demo.domain.user.dto.request.UserUpdateRequest;
import com.example.demo.domain.user.dto.response.UserInfoResponse;
import com.example.demo.domain.user.dto.response.UserUpdateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserInfoResponse> getUserProfile(@AuthenticationPrincipal UserPrincipal user) {

        return ResponseEntity.ok(userService.getUserProfile(user));
    }

    @PatchMapping("/profile")
    public ResponseEntity<UserUpdateResponse> updateUserProfile(@AuthenticationPrincipal UserPrincipal user,
                                                                @RequestBody @Valid UserUpdateRequest request) {

        return ResponseEntity.ok(userService.updateUserProfile(user, request));
    }

    @PatchMapping("/profile-password")
    public ResponseEntity<Void> updateUserPassword(@AuthenticationPrincipal UserPrincipal user,
                                                                 @RequestBody @Valid UserPasswordUpdateRequest request) {
        userService.updateUserPassword(user, request);
        return ResponseEntity.ok().build();
    }
}
