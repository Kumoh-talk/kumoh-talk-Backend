package com.example.demo.domain.recruitment_application.controller;

import com.example.demo.domain.recruitment_application.domain.request.RecruitmentApplicationRequest;
import com.example.demo.domain.recruitment_application.domain.response.MyRecruitmentApplicationPageResponse;
import com.example.demo.domain.recruitment_application.domain.response.RecruitmentApplicantPageResponse;
import com.example.demo.domain.recruitment_application.domain.response.RecruitmentApplicationResponse;
import com.example.demo.domain.recruitment_application.service.RecruitmentApplicationService;
import com.example.demo.domain.recruitment_board.domain.dto.vo.BoardType;
import com.example.demo.domain.recruitment_board.domain.dto.vo.RecruitmentBoardType;
import com.example.demo.global.aop.AssignUserId;
import com.example.demo.global.base.dto.ResponseBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.global.base.dto.ResponseUtil.createSuccessResponse;

@RestController
@RequestMapping("/api/v1/applications/recruitment")
@RequiredArgsConstructor
public class RecruitmentApplicationController {
    private final RecruitmentApplicationService recruitmentApplicationService;

    /**
     * 신청 추가
     */
    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_ACTIVE_USER')")
    @PostMapping("/{recruitmentBoardId}")
    public ResponseEntity<ResponseBody<RecruitmentApplicationResponse>> createApplication(
            Long userId,
            @PathVariable Long recruitmentBoardId,
            @RequestBody @Valid RecruitmentApplicationRequest request) {
        return ResponseEntity.ok(createSuccessResponse(recruitmentApplicationService.createApplication(userId, recruitmentBoardId, request)));
    }

    /**
     * 게시물 별 신청 리스트 조회(applicant 테이블 get) -> 관리자 기능
     *
     * @param : size(페이징 사이즈), page(페이지 번호)
     */
    // TODO : 디자인이 확정되면 응답 포맷 확실히
    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_ACTIVE_USER')")
    @GetMapping("/{recruitmentBoardId}")
    public ResponseEntity<ResponseBody<RecruitmentApplicantPageResponse>> getApplicantList(
            Long userId,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable Long recruitmentBoardId) {
        return ResponseEntity.ok(createSuccessResponse(recruitmentApplicationService.getApplicantList(userId, pageable, recruitmentBoardId)));
    }

    /**
     * applicant id를 사용하여 신청 정보 상세 조회 -> 관리자 기능
     */
    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_ACTIVE_USER')")
    @GetMapping("/{recruitmentBoardId}/{applicantId}")
    public ResponseEntity<ResponseBody<RecruitmentApplicationResponse>> getApplicationInfoByApplicantId(
            Long userId,
            @PathVariable Long recruitmentBoardId,
            @PathVariable Long applicantId) {
        return ResponseEntity.ok(createSuccessResponse(recruitmentApplicationService.getApplicationInfo(userId, recruitmentBoardId, applicantId)));
    }

    /**
     * 해당 신청 게시물에 신청한 내용 수정 -> 마감기한 지나면 불가능
     */
    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_ACTIVE_USER')")
    @PatchMapping("/{applicantId}")
    public ResponseEntity<ResponseBody<RecruitmentApplicationResponse>> updateApplication(
            Long userId,
            @PathVariable Long applicantId,
            @RequestBody @Valid RecruitmentApplicationRequest request) {
        return ResponseEntity.ok(createSuccessResponse(recruitmentApplicationService.updateApplication(userId, applicantId, request)));
    }

    /**
     * 해당 신청 게시물에 신청한 내용 삭제 -> 마감기한 지나면 불가능
     */
    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_ACTIVE_USER')")
    @DeleteMapping("/{applicantId}")
    public ResponseEntity<ResponseBody<Void>> deleteApplication(
            Long userId,
            @PathVariable Long applicantId) {
        recruitmentApplicationService.deleteApplication(userId, applicantId);
        return ResponseEntity.ok(createSuccessResponse());
    }

    /**
     * user id를 사용하여 신청 리스트 조회(마이페이지)
     *
     * @param : size(페이징 사이즈), page(페이지 번호), boardType[study, project, mentoring]
     */
    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_ACTIVE_USER')")
    @GetMapping("/my-applications")
    public ResponseEntity<ResponseBody<MyRecruitmentApplicationPageResponse>> getUserApplicationList(
            Long userId,
            @RequestParam BoardType boardType,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(createSuccessResponse(recruitmentApplicationService.getUserApplicationList(userId, pageable, RecruitmentBoardType.valueOf(boardType.name()))));
    }
}
