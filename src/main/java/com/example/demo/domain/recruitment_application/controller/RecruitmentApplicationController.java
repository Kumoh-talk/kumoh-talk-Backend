package com.example.demo.domain.recruitment_application.controller;

import com.example.demo.domain.recruitment_application.domain.dto.request.RecruitmentApplicationRequest;
import com.example.demo.domain.recruitment_application.domain.dto.response.MyRecruitmentApplicationPageResponse;
import com.example.demo.domain.recruitment_application.domain.dto.response.RecruitmentApplicantPageResponse;
import com.example.demo.domain.recruitment_application.domain.dto.response.RecruitmentApplicationResponse;
import com.example.demo.domain.recruitment_application.service.RecruitmentApplicationService;
import com.example.demo.domain.recruitment_board.domain.vo.BoardType;
import com.example.demo.domain.recruitment_board.domain.vo.RecruitmentBoardType;
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
     * [모집 신청 추가] <br>
     * 모집 공고에 신청서 추가
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
     * [모집 게시물 별 신청 페이징 리스트 조회] <br>
     * 페이지 번호로 구현된 모집 게시물 작성자가 화인할 수 있는 신청 페이징 리스트 조회
     *
     * @param pageable 페이지 번호(page), 페이지 사이즈(size), 페이지 정렬 조건 및 정렬 방향(sort) <br>
     *                 -> 정렬 조건은 createdAt <br>
     *                 -> 정렬 방향은 asc, desc 중 선택
     */
    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_ACTIVE_USER')")
    @GetMapping("/{recruitmentBoardId}")
    public ResponseEntity<ResponseBody<RecruitmentApplicantPageResponse>> getApplicantList(
            Long userId,
            @PathVariable Long recruitmentBoardId,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(createSuccessResponse(recruitmentApplicationService.getApplicantList(userId, pageable, recruitmentBoardId, false)));
    }

    /**
     * [신청서 정보 상세 조회] <br>
     * 신청서 목록 창에서 applicantId를 사용하여 해당 신청자의 신청서 정보를 상세 조회
     *
     * @apiNote applicant 테이블 도입 이유 -> recruitment_applicants_answers 테이블에 저장된 신청자들의 답변 중 동일한 신청서의 답변들을 묶기 위해
     */
    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_ACTIVE_USER')")
    @GetMapping("/{recruitmentBoardId}/{applicantId}")
    public ResponseEntity<ResponseBody<RecruitmentApplicationResponse>> getApplicationInfoByApplicantId(
            Long userId,
            @PathVariable Long recruitmentBoardId,
            @PathVariable Long applicantId) {
        return ResponseEntity.ok(createSuccessResponse(recruitmentApplicationService.getApplicationInfo(userId, recruitmentBoardId, applicantId, false)));
    }

    /**
     * [신청서 정보 변경] <br>
     * 마감기한 전일 경우 신청자가 신청서 정보를 변경가능
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
     * [지정 신청서 삭제] <br>
     * 마감기한 전일 경우 신청자가 자신의 신청서를 삭제
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
     * [사용자 신청 페이징 리스트 조회] <br>
     * 페이지 번호로 구현된 마이페이지에 출력될 신청 페이징 리스트 조회
     *
     * @param pageable 페이지 번호(page), 페이지 사이즈(size), 페이지 정렬 조건 및 정렬 방향(sort) <br>
     *                 -> 정렬 조건은 createdAt <br>
     *                 -> 정렬 방향은 asc, desc 중 선택
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
