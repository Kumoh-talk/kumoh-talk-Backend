package com.example.demo.domain.study_project_application.controller;

import com.example.demo.domain.study_project_application.domain.request.StudyProjectApplicationRequest;
import com.example.demo.domain.study_project_application.domain.response.MyStudyProjectApplicationPageResponse;
import com.example.demo.domain.study_project_application.domain.response.StudyProjectApplicantPageResponse;
import com.example.demo.domain.study_project_application.domain.response.StudyProjectApplicationResponse;
import com.example.demo.domain.study_project_application.service.StudyProjectApplicationService;
import com.example.demo.domain.study_project_board.domain.dto.vo.BoardType;
import com.example.demo.domain.study_project_board.domain.dto.vo.StudyProjectBoardType;
import com.example.demo.global.aop.AssignUserId;
import com.example.demo.global.base.dto.ResponseBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.global.base.dto.ResponseUtil.createSuccessResponse;

@RestController
@RequestMapping("/api/v1/applications/study-project")
@RequiredArgsConstructor
public class StudyProjectApplicationController {
    private final StudyProjectApplicationService studyProjectApplicationService;

    /**
     * 신청 추가
     */
    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_ACTIVE_USER')")
    @PostMapping("/{studyProjectBoardId}")
    public ResponseEntity<ResponseBody<StudyProjectApplicationResponse>> createApplication(
            Long userId,
            @PathVariable Long studyProjectBoardId,
            @RequestBody @Valid StudyProjectApplicationRequest request) {
        return ResponseEntity.ok(createSuccessResponse(studyProjectApplicationService.createApplication(userId, studyProjectBoardId, request)));
    }

    /**
     * 게시물 별 신청 리스트 조회(applicant 테이블 get) -> 관리자 기능
     *
     * @param : size(페이징 사이즈), page(페이지 번호)
     */
    // TODO : 디자인이 확정되면 응답 포맷 확실히
    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_ACTIVE_USER')")
    @GetMapping("/{studyProjectBoardId}")
    public ResponseEntity<ResponseBody<StudyProjectApplicantPageResponse>> getApplicantList(
            Long userId,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable Long studyProjectBoardId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userRole = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()  // 첫 번째 역할을 가져옴 (필요에 따라 수정 가능)
                .orElse(null);

        return ResponseEntity.ok(createSuccessResponse(studyProjectApplicationService.getApplicantList(userId, pageable, studyProjectBoardId, userRole)));
    }

    /**
     * applicant id를 사용하여 신청 정보 상세 조회 -> 관리자 기능
     */
    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_ACTIVE_USER')")
    @GetMapping("/{studyProjectBoardId}/{applicantId}")
    public ResponseEntity<ResponseBody<StudyProjectApplicationResponse>> getApplicationInfoByApplicantId(
            Long userId,
            @PathVariable Long studyProjectBoardId,
            @PathVariable Long applicantId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userRole = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()  // 첫 번째 역할을 가져옴 (필요에 따라 수정 가능)
                .orElse(null);

        return ResponseEntity.ok(createSuccessResponse(studyProjectApplicationService.getApplicationInfo(userId, studyProjectBoardId, applicantId, userRole)));
    }

    /**
     * 해당 신청 게시물에 신청한 내용 수정 -> 마감기한 지나면 불가능
     */
    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_ACTIVE_USER')")
    @PatchMapping("/{applicantId}")
    public ResponseEntity<ResponseBody<StudyProjectApplicationResponse>> updateApplication(
            Long userId,
            @PathVariable Long applicantId,
            @RequestBody @Valid StudyProjectApplicationRequest request) {
        return ResponseEntity.ok(createSuccessResponse(studyProjectApplicationService.updateApplication(userId, applicantId, request)));
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
        studyProjectApplicationService.deleteApplication(userId, applicantId);
        return ResponseEntity.ok(createSuccessResponse());
    }

    /**
     * user id를 사용하여 신청 리스트 조회(마이페이지)
     *
     * @param : size(페이징 사이즈), page(페이지 번호), boardType[study, project]
     */
    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_ACTIVE_USER')")
    @GetMapping("/my-applications")
    public ResponseEntity<ResponseBody<MyStudyProjectApplicationPageResponse>> getUserApplicationList(
            Long userId,
            @RequestParam BoardType boardType,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(createSuccessResponse(studyProjectApplicationService.getUserApplicationList(userId, pageable, StudyProjectBoardType.valueOf(boardType.name()))));
    }
}
