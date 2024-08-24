package com.example.demo.domain.seminar_application.controller;

import com.example.demo.domain.seminar_application.domain.dto.request.SeminarApplicationRequest;
import com.example.demo.domain.seminar_application.domain.dto.request.SeminarApplicationUpdateRequest;
import com.example.demo.domain.seminar_application.domain.dto.response.SeminarApplicationInfo;
import com.example.demo.domain.seminar_application.service.SeminarApplicationService;
import com.example.demo.global.aop.AssignUserId;
import com.example.demo.global.base.dto.ResponseBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.global.base.dto.ResponseUtil.createSuccessResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/seminar-application")
public class SeminarApplicationController {

    private final SeminarApplicationService seminarApplicationService;

    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @PostMapping("/apply")
    public ResponseEntity<ResponseBody<Void>> applyForSeminar(Long userId,
                                                              @RequestBody @Valid SeminarApplicationRequest request) {
        seminarApplicationService.applyForSeminar(userId, request);
        return ResponseEntity.ok(createSuccessResponse());
    }

    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/apply")
    public ResponseEntity<ResponseBody<Page<SeminarApplicationInfo>>> getSeminarApplicationByUserId(Long userId,
                                                                                                    Pageable pageable) {
        return ResponseEntity.ok(createSuccessResponse(seminarApplicationService.getSeminarApplicationByUserId(userId, pageable)));
    }

    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @PutMapping("/apply/{seminarApplicationId}")
    public ResponseEntity<ResponseBody<Void>> updateSeminarApplication(Long userId,
                                                                       @PathVariable Long seminarApplicationId,
                                                                       @RequestBody @Valid SeminarApplicationUpdateRequest request) {
        seminarApplicationService.updateSeminarApplication(userId, seminarApplicationId, request);
        return ResponseEntity.ok(createSuccessResponse());
    }

    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @DeleteMapping("/apply/{seminarApplicationId}")
    public ResponseEntity<ResponseBody<Void>> deleteSeminarApplication(Long userId,
                                                                       @PathVariable Long seminarApplicationId) {
        seminarApplicationService.deleteSeminarApplication(userId, seminarApplicationId);
        return ResponseEntity.ok(createSuccessResponse());
    }
}
