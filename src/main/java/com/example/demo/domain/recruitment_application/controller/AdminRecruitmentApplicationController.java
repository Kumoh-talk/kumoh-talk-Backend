package com.example.demo.domain.recruitment_application.controller;

import com.example.demo.domain.recruitment_application.domain.dto.response.RecruitmentApplicantPageResponse;
import com.example.demo.domain.recruitment_application.domain.dto.response.RecruitmentApplicationResponse;
import com.example.demo.domain.recruitment_application.service.RecruitmentApplicationService;
import com.example.demo.global.base.dto.ResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.demo.global.base.dto.ResponseUtil.createSuccessResponse;

@RestController
@RequestMapping("/api/v1/admin/applications/recruitment")
@RequiredArgsConstructor
public class AdminRecruitmentApplicationController {
    private final RecruitmentApplicationService recruitmentApplicationService;

    /**
     * 관리자 전용 게시물 별 신청 리스트 조회(applicant 테이블 get)
     *
     * @param : size(페이징 사이즈), page(페이지 번호)
     */
    // TODO : 디자인이 확정되면 응답 포맷 확실히
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/{recruitmentBoardId}")
    public ResponseEntity<ResponseBody<RecruitmentApplicantPageResponse>> getApplicantListByAdmin(
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable Long recruitmentBoardId) {
        return ResponseEntity.ok(createSuccessResponse(recruitmentApplicationService.getApplicantList(null, pageable, recruitmentBoardId, true)));
    }

    /**
     * 관리자 전용 applicant id를 사용하여 신청 정보 상세 조회
     */
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/{recruitmentBoardId}/{applicantId}")
    public ResponseEntity<ResponseBody<RecruitmentApplicationResponse>> getApplicationInfoByApplicantIdByAdmin(
            @PathVariable Long recruitmentBoardId,
            @PathVariable Long applicantId) {
        return ResponseEntity.ok(createSuccessResponse(recruitmentApplicationService.getApplicationInfo(null, recruitmentBoardId, applicantId, true)));
    }
}
