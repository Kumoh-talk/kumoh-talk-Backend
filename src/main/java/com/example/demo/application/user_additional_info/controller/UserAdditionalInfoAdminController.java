package com.example.demo.application.user_additional_info.controller;

import com.example.demo.application.user_additional_info.api.UserAdditionalInfoAdminApi;
import com.example.demo.application.user_additional_info.dto.response.UserAdditionalInfoResponse;
import com.example.demo.domain.user_addtional_info.service.UserAdditionalInfoAdminService;
import com.example.demo.global.base.dto.ResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.demo.global.base.dto.ResponseUtil.createSuccessResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/admin/userAdditionalInfos")
@PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
public class UserAdditionalInfoAdminController implements UserAdditionalInfoAdminApi {

    private final UserAdditionalInfoAdminService userAdditionalInfoAdminService;

//    @GetMapping
//    public ResponseEntity<ResponseBody<GlobalPageResponse<UserAdditionalInfoResponse>>> getAllUserAdditionalInfos (
//            @PageableDefault(page=0, size=10,sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
//    ) {
//        return ResponseEntity.ok(createSuccessResponse(userAdditionalInfoAdminService.getAllUserAdditionalInfos(pageable)));
//    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseBody<UserAdditionalInfoResponse>> getUserAdditionalInfo (@PathVariable Long userId) {
        return ResponseEntity.ok(createSuccessResponse(userAdditionalInfoAdminService.getUserAdditionalInfo(userId)));
    }


}
