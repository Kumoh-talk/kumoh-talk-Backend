package com.example.demo.domain.study_project_board.controller;

import com.example.demo.domain.board.domain.dto.vo.Status;
import com.example.demo.domain.study_project_board.domain.dto.request.StudyProjectBoardInfoAndFormRequest;
import com.example.demo.domain.study_project_board.domain.dto.response.*;
import com.example.demo.domain.study_project_board.domain.dto.vo.StudyProjectBoardType;
import com.example.demo.domain.study_project_board.service.StudyProjectBoardService;
import com.example.demo.global.aop.AssignUserId;
import com.example.demo.global.base.dto.ResponseBody;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static com.example.demo.global.base.dto.ResponseUtil.createSuccessResponse;

@RestController
@RequestMapping("/api/v1/study-project-boards")
@RequiredArgsConstructor
public class StudyProjectBoardController {
    private final StudyProjectBoardService studyProjectBoardService;
    private final Validator validator;

    // TODO : 마감기한이 지한 게시물은 삭제 처리?
    // TODO : 게시물이 수정되어 질문이 변경된다면, 이미 신청한 신청자들은 어떻게 되는가? -> 신청자들에게 알림을 주는 서비스?

    /**
     * 게시물 저장 및 임시저장 API
     *
     * @param : status[published, draft]
     */
    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_ACTIVE_USER')")
    @PostMapping()
    public ResponseEntity<ResponseBody<StudyProjectBoardInfoAndFormResponse>> createStudyProjectBoardAndForm(
            Long userId,
            @RequestParam Status status,
            @RequestBody StudyProjectBoardInfoAndFormRequest studyProjectBoardInfoAndFormRequest) throws MethodArgumentNotValidException {
        validateStudyProjectBoardInfoAndFormRequest(status, studyProjectBoardInfoAndFormRequest);
        return ResponseEntity.ok(createSuccessResponse(studyProjectBoardService.saveBoardAndForm(userId, studyProjectBoardInfoAndFormRequest, status)));
    }

    /**
     * 홈 화면 스터디, 프로젝트 Published 게시물 리스트 조회 API(No-Offset)
     *
     * @param : size(페이징 사이즈), lastBoardId(전 페이지 마지막 게시물 Id), boardType[study, project]
     */
    @GetMapping("/no-offset")
    public ResponseEntity<ResponseBody<StudyProjectBoardNoOffsetResponse>> getStudyProjectBoardListByNoOffset(
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long lastBoardId,
            @RequestParam StudyProjectBoardType boardType
    ) {
        // TODO : 차단 기능 추가
        return ResponseEntity.ok(createSuccessResponse(studyProjectBoardService.getPublishedBoardListByNoOffset(size, lastBoardId, boardType)));
    }

    /**
     * 더보기 스터디, 프로젝트 Published 게시물 리스트 조회 API(PageNum)
     *
     * @param : size, page, boardType[study, project]
     */
    @GetMapping("/page-num")
    public ResponseEntity<ResponseBody<StudyProjectBoardPageNumResponse>> getStudyProjectBoardListByPageNum(
            @PageableDefault(page = 0, size = 10, sort = "recruitmentDeadline", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam StudyProjectBoardType boardType
    ) {
        return ResponseEntity.ok(createSuccessResponse(studyProjectBoardService.getPublishedBoardListByPageNum(pageable, boardType)));
    }

    /**
     * 스터디, 프로젝트 게시물 상세조회 API
     */
    @GetMapping("/{studyProjectBoardId}/board")
    public ResponseEntity<ResponseBody<StudyProjectBoardInfoResponse>> getStudyProjectBoardInfo(@PathVariable Long studyProjectBoardId) {
        return ResponseEntity.ok(createSuccessResponse(studyProjectBoardService.getBoardInfo(studyProjectBoardId)));
    }

    /**
     * 스터디, 프로젝트 신청폼 상세조회 API
     */
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_ACTIVE_USER')")
    @GetMapping("/{studyProjectBoardId}/form")
    public ResponseEntity<ResponseBody<List<StudyProjectFormQuestionResponse>>> getStudyProjectFormInfo(@PathVariable Long studyProjectBoardId) {
        return ResponseEntity.ok(createSuccessResponse(studyProjectBoardService.getFormInfoList(studyProjectBoardId)));
    }

    /**
     * 스터디, 프로젝트 게시물 및 신청폼 수정 API
     *
     * @param : status[published, draft]
     */
    // 게시물 작성 전 임시저장 게시물을 불러온 후 저장하면 해당 API 요청
    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_ACTIVE_USER')")
    @PatchMapping("/{studyProjectBoardId}")
    public ResponseEntity<ResponseBody<StudyProjectBoardInfoAndFormResponse>> updateStudyProjectBoardAndForm(
            Long userId,
            @PathVariable Long studyProjectBoardId,
            @RequestParam Status status,
            @RequestBody StudyProjectBoardInfoAndFormRequest studyProjectBoardInfoAndFormRequest) throws MethodArgumentNotValidException {
        validateStudyProjectBoardInfoAndFormRequest(status, studyProjectBoardInfoAndFormRequest);
        return ResponseEntity.ok(createSuccessResponse(studyProjectBoardService.updateBoardAndForm(userId, studyProjectBoardId, studyProjectBoardInfoAndFormRequest, status)));

    }

    /**
     * 스터디, 프로젝트 게시물 및 신청폼 삭제 API
     */
    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_ACTIVE_USER')")
    @DeleteMapping("/{studyProjectBoardId}")
    public ResponseEntity<ResponseBody<Void>> deleteStudyProjectBoardAndForm(
            Long userId,
            @PathVariable Long studyProjectBoardId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userRole = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()  // 첫 번째 역할을 가져옴 (필요에 따라 수정 가능)
                .orElse(null);

        studyProjectBoardService.deleteBoardAndForm(userId, studyProjectBoardId, userRole);
        return ResponseEntity.ok(createSuccessResponse());
    }

    /**
     * 최근 임시저장 게시물 get
     */
    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_ACTIVE_USER')")
    @GetMapping("/draft/latest")
    public ResponseEntity<ResponseBody<StudyProjectBoardInfoAndFormResponse>> getDraftStudyProjectBoard(
            Long userId) {
        return ResponseEntity.ok(createSuccessResponse(studyProjectBoardService.getLatestDraftBoardAndForm(userId)));
    }

    /**
     * 사용자의 임시저장 게시물 목록 get(No-Offset)
     */
    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_ACTIVE_USER')")
    @GetMapping("/draft")
    public ResponseEntity<ResponseBody<StudyProjectBoardNoOffsetResponse>> getDraftStudyProjectBoardList(
            Long userId,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long lastBoardId) {
        return ResponseEntity.ok(createSuccessResponse(studyProjectBoardService.getDraftBoardListByUserId(userId, size, lastBoardId)));
    }

    /**
     * 사용자가 작성한 글 리스트 조회(PageNum)
     *
     * @param : size, page, boardType[study, project]
     */
    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_ACTIVE_USER')")
    @GetMapping("/my-boards")
    public ResponseEntity<ResponseBody<StudyProjectBoardPageNumResponse>> getPublishedUserStudyProjectBoardList(
            Long userId,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam StudyProjectBoardType boardType) {
        return ResponseEntity.ok(createSuccessResponse(
                studyProjectBoardService.getPublishedBoardListByUserId(userId, pageable, boardType)));
    }

    // Valid 검사 메서드
    // 임시저장은 valid 검사를 하지 않음
    private void validateStudyProjectBoardInfoAndFormRequest(Status status, @Valid StudyProjectBoardInfoAndFormRequest request) throws MethodArgumentNotValidException {
        switch (status) {
            case PUBLISHED -> {
                Set<ConstraintViolation<StudyProjectBoardInfoAndFormRequest>> violations = validator.validate(request);
                if (!violations.isEmpty()) {
                    BindingResult bindingResult = new BeanPropertyBindingResult(request, "studyProjectBoardInfoAndFormRequest");
                    for (ConstraintViolation<StudyProjectBoardInfoAndFormRequest> violation : violations) {
                        bindingResult.addError(new ObjectError("studyProjectBoardInfoAndFormRequest", violation.getMessage()));
                    }
                    throw new MethodArgumentNotValidException(null, bindingResult);
                }
            }
            case DRAFT -> {
                return;
            }
        }
    }
}
