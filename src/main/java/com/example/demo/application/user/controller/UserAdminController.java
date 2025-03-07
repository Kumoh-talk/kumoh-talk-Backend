package com.example.demo.application.user.controller;

import com.example.demo.application.user.api.UserAdminApi;
import com.example.demo.application.user.dto.request.UpdateUserInfoRequest;
import com.example.demo.application.user.dto.response.UserInfoResponse;
import com.example.demo.domain.base.page.GlobalPageableDto;
import com.example.demo.domain.user.entity.UpdateUserInfo;
import com.example.demo.domain.user.entity.UserInfo;
import com.example.demo.domain.user.service.UserAdminService;
import com.example.demo.global.base.dto.ResponseBody;
import com.example.demo.global.base.dto.page.GlobalPageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<ResponseBody<GlobalPageResponse<UserInfoResponse>>> getAllUsers (
            @PageableDefault(page=0, size=10,sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        GlobalPageableDto globalPageableDto = GlobalPageableDto.create(pageable);
        GlobalPageableDto response = userAdminService.getAllUsers(globalPageableDto);
        return ResponseEntity.ok(createSuccessResponse(GlobalPageResponse.create(response)));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<ResponseBody<Void>> updateUserInfo (@PathVariable Long userId, @Valid @RequestBody UpdateUserInfoRequest request) {
        UpdateUserInfo updateUserInfo = UpdateUserInfoRequest.toUpdateUserInfo(request);
        userAdminService.updateUserInfo(userId, updateUserInfo);
        return ResponseEntity.ok(createSuccessResponse());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ResponseBody<Void>> deleteUser (@PathVariable Long userId) {
        userAdminService.deleteUser(userId);
        return ResponseEntity.ok(createSuccessResponse());
    }
}
