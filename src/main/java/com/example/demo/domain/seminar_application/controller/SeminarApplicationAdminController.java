package com.example.demo.domain.seminar_application.controller;

import com.example.demo.domain.seminar_application.api.SeminarApplicationAdminApi;
import com.example.demo.domain.seminar_application.domain.dto.response.SeminarApplicationInfo;
import com.example.demo.domain.seminar_application.service.SeminarApplicationAdminService;
import com.example.demo.domain.user.domain.dto.response.UserInfo;
import com.example.demo.global.base.dto.ResponseBody;
import com.example.demo.global.base.dto.page.GlobalPageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.demo.global.base.dto.ResponseUtil.createSuccessResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/admin/seminar-applications")
@PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
public class SeminarApplicationAdminController implements SeminarApplicationAdminApi {

    private final SeminarApplicationAdminService seminarApplicationAdminService;

    @GetMapping
    public ResponseEntity<ResponseBody<GlobalPageResponse<SeminarApplicationInfo>>> getAllSeminarApplications (
            @PageableDefault(page=0, size=10,sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(createSuccessResponse(seminarApplicationAdminService.getAllSeminarApplications(pageable)));
    }


}
