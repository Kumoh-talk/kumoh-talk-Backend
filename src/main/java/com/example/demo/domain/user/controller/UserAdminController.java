package com.example.demo.domain.user.controller;

import com.example.demo.domain.user.api.UserAdminApi;
import com.example.demo.domain.user.domain.dto.request.UpdateUserInfoRequest;
import com.example.demo.domain.user.domain.dto.response.UserInfo;
import com.example.demo.domain.user.service.UserAdminService;
import com.example.demo.global.base.dto.ResponseBody;
import com.example.demo.global.base.dto.page.GlobalPageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.global.base.dto.ResponseUtil.createSuccessResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/admin/users")
@PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
public class UserAdminController implements UserAdminApi {

    private final UserAdminService userAdminService;

    @GetMapping
    public ResponseEntity<ResponseBody<GlobalPageResponse<UserInfo>>> getAllUsers (
            @PageableDefault(page=0, size=10,sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(createSuccessResponse(userAdminService.getAllUsers(pageable)));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<ResponseBody<Void>> updateUserInfo (@PathVariable Long userId, @Valid @RequestBody UpdateUserInfoRequest request) {
        userAdminService.updateUserInfo(userId, request);
        return ResponseEntity.ok(createSuccessResponse());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ResponseBody<Void>> deleteUser (@PathVariable Long userId) {
        userAdminService.deleteUser(userId);
        return ResponseEntity.ok(createSuccessResponse());
    }
}
