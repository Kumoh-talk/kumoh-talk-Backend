package com.example.demo.domain.user.api;


import com.example.demo.domain.auth.domain.UserPrincipal;
import com.example.demo.domain.user.application.UserService;
import com.example.demo.domain.user.dto.response.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserInfoResponse> getUserProfile(@AuthenticationPrincipal UserPrincipal user) {

        return ResponseEntity.ok(userService.getUserProfile(user));
    }
}
