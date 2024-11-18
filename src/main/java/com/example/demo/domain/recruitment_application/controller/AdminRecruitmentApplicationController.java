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
     * [관리자 전용 모집 게시물 별 신청 페이징 리스트 조회] <br>
     * 페이지 번호로 구현된 모집 게시물 작성자가 화인할 수 있는 신청 페이징 리스트 조회
     *
     * @param pageable 페이지 번호(page), 페이지 사이즈(size), 페이지 정렬 조건 및 정렬 방향(sort) <br>
     *                 -> 정렬 조건은 createdAt <br>
     *                 -> 정렬 방향은 asc, desc 중 선택
     * @apiNote 1. 필터에서 유저 권한이 ADMIN인 것을 확인하면 따로 서비스 로직에서 유저 인증을 거치지 않도록 isAuthorized 매개변수를 true로 하여 서비스 메서드를 호출
     */
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/{recruitmentBoardId}")
    public ResponseEntity<ResponseBody<RecruitmentApplicantPageResponse>> getApplicantListByAdmin(
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable Long recruitmentBoardId) {
        return ResponseEntity.ok(createSuccessResponse(recruitmentApplicationService.getApplicantList(null, pageable, recruitmentBoardId, true)));
    }

    /**
     * [관리자 전용 신청서 정보 상세 조회] <br>
     * 신청서 목록 창에서 applicantId를 사용하여 해당 신청자의 신청서 정보를 상세 조회
     *
     * @apiNote 1. applicant 테이블 도입 이유 -> recruitment_applicants_answers 테이블에 저장된 신청자들의 답변 중 동일한 신청서의 답변들을 묶기 위해 <br>
     * 2. 필터에서 유저 권한이 ADMIN인 것을 확인하면 따로 서비스 로직에서 유저 인증을 거치지 않도록 isAuthorized 매개변수를 true로 하여 서비스 메서드를 호출
     */
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/{recruitmentBoardId}/{applicantId}")
    public ResponseEntity<ResponseBody<RecruitmentApplicationResponse>> getApplicationInfoByApplicantIdByAdmin(
            @PathVariable Long recruitmentBoardId,
            @PathVariable Long applicantId) {
        return ResponseEntity.ok(createSuccessResponse(recruitmentApplicationService.getApplicationInfo(null, recruitmentBoardId, applicantId, true)));
    }
}
