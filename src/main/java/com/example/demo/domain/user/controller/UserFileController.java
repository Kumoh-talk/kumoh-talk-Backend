package com.example.demo.domain.user.controller;

import com.example.demo.domain.board.domain.dto.request.FileRequest;
import com.example.demo.domain.user.domain.dto.request.ChangeProfileUrlRequest;
import com.example.demo.domain.user.domain.dto.request.ProfilePresignedUrlRequest;
import com.example.demo.domain.user.service.UserFileService;
import com.example.demo.global.aop.AssignUserId;
import com.example.demo.global.base.dto.ResponseBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.global.base.dto.ResponseUtil.createSuccessResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/files")
public class UserFileController {

    private final UserFileService userFileService;

    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    @PostMapping("/presigned-url")
    public ResponseEntity<ResponseBody<String>> getPresignedUrl(Long userId,
                                                                @RequestBody @Valid ProfilePresignedUrlRequest request) {
        return ResponseEntity.ok(createSuccessResponse(userFileService.getPresignedUrl(userId, request)));
    }

    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    @PatchMapping("/profile")
    public ResponseEntity<ResponseBody<Void>> changeProfileUrl(Long userId,@RequestBody @Valid ChangeProfileUrlRequest request) {
        userFileService.changeProfileUrl(userId, request);
        return ResponseEntity.ok(createSuccessResponse());
    }
}
