package com.example.demo.domain.recruitment_board.controller;

import com.example.demo.domain.board.domain.dto.vo.Status;
import com.example.demo.domain.recruitment_board.domain.dto.request.RecruitmentBoardInfoAndFormRequest;
import com.example.demo.domain.recruitment_board.domain.dto.response.*;
import com.example.demo.domain.recruitment_board.domain.dto.vo.RecruitmentBoardType;
import com.example.demo.domain.recruitment_board.service.RecruitmentBoardService;
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
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static com.example.demo.global.base.dto.ResponseUtil.createSuccessResponse;

@RestController
@RequestMapping("/api/v1/recruitment-boards")
@RequiredArgsConstructor
public class RecruitmentBoardController {
    private final RecruitmentBoardService recruitmentBoardService;
    private final Validator validator;

    // TODO : 마감기한이 지한 게시물은 삭제 처리?

    /**
     * 게시물 저장 및 임시저장 API
     *
     * @param : status[published, draft]
     */
    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_ACTIVE_USER')")
    @PostMapping()
    public ResponseEntity<ResponseBody<RecruitmentBoardInfoAndFormResponse>> createRecruitmentBoardAndForm(
            Long userId,
            @RequestParam Status status,
            @RequestBody RecruitmentBoardInfoAndFormRequest recruitmentBoardInfoAndFormRequest) throws MethodArgumentNotValidException {
        validateRecruitmentBoardInfoAndFormRequest(status, recruitmentBoardInfoAndFormRequest);
        return ResponseEntity.ok(createSuccessResponse(recruitmentBoardService.saveBoardAndForm(userId, recruitmentBoardInfoAndFormRequest, status)));
    }

    /**
     * 홈 화면 스터디, 프로젝트, 멘토링 Published 게시물 리스트 조회 API(No-Offset)
     *
     * @param : size(페이징 사이즈), lastBoardId(전 페이지 마지막 게시물 Id), boardType[study, project, mentoring]
     */
    @GetMapping("/no-offset")
    public ResponseEntity<ResponseBody<RecruitmentBoardNoOffsetResponse>> getRecruitmentBoardListByNoOffset(
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long lastBoardId,
            @RequestParam RecruitmentBoardType boardType
    ) {
        // TODO : 차단 기능 추가
        return ResponseEntity.ok(createSuccessResponse(recruitmentBoardService.getPublishedBoardListByNoOffset(size, lastBoardId, boardType)));
    }

    /**
     * 더보기 스터디, 프로젝트, 멘토링 Published 게시물 리스트 조회 API(PageNum)
     *
     * @param : size, page, boardType[study, project, mentoring]
     */
    @GetMapping("/page-num")
    public ResponseEntity<ResponseBody<RecruitmentBoardPageNumResponse>> getRecruitmentBoardListByPageNum(
            @PageableDefault(page = 0, size = 10, sort = "recruitmentDeadline", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam RecruitmentBoardType boardType
    ) {
        return ResponseEntity.ok(createSuccessResponse(recruitmentBoardService.getPublishedBoardListByPageNum(pageable, boardType)));
    }

    /**
     * 스터디, 프로젝트, 멘토링 게시물 상세조회 API
     */
    @GetMapping("/{recruitmentBoardId}/board")
    public ResponseEntity<ResponseBody<RecruitmentBoardInfoResponse>> getRecruitmentBoardInfo(@PathVariable Long recruitmentBoardId) {
        return ResponseEntity.ok(createSuccessResponse(recruitmentBoardService.getBoardInfo(recruitmentBoardId)));
    }

    /**
     * 스터디, 프로젝트, 멘토링 신청폼 상세조회 API
     */
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_ACTIVE_USER')")
    @GetMapping("/{recruitmentBoardId}/form")
    public ResponseEntity<ResponseBody<List<RecruitmentFormQuestionResponse>>> getRecruitmentFormInfo(@PathVariable Long recruitmentBoardId) {
        return ResponseEntity.ok(createSuccessResponse(recruitmentBoardService.getFormInfoList(recruitmentBoardId)));
    }

    /**
     * 스터디, 프로젝트, 멘토링 게시물 및 신청폼 수정 API
     *
     * @param : status[published, draft]
     */
    // 게시물 작성 전 임시저장 게시물을 불러온 후 저장하면 해당 API 요청
    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_ACTIVE_USER')")
    @PatchMapping("/{recruitmentBoardId}")
    public ResponseEntity<ResponseBody<RecruitmentBoardInfoAndFormResponse>> updateRecruitmentBoardAndForm(
            Long userId,
            @PathVariable Long recruitmentBoardId,
            @RequestParam Status status,
            @RequestBody RecruitmentBoardInfoAndFormRequest recruitmentBoardInfoAndFormRequest) throws MethodArgumentNotValidException {
        validateRecruitmentBoardInfoAndFormRequest(status, recruitmentBoardInfoAndFormRequest);
        return ResponseEntity.ok(createSuccessResponse(recruitmentBoardService.updateBoardAndForm(userId, recruitmentBoardId, recruitmentBoardInfoAndFormRequest, status)));

    }

    /**
     * 스터디, 프로젝트, 멘토링 게시물 및 신청폼 삭제 API
     */
    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_ACTIVE_USER')")
    @DeleteMapping("/{recruitmentBoardId}")
    public ResponseEntity<ResponseBody<Void>> deleteRecruitmentBoardAndForm(
            Long userId,
            @PathVariable Long recruitmentBoardId) {
        recruitmentBoardService.deleteBoardAndForm(userId, recruitmentBoardId, false);
        return ResponseEntity.ok(createSuccessResponse());
    }

    /**
     * 최근 임시저장 게시물 get
     */
    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_ACTIVE_USER')")
    @GetMapping("/draft/latest")
    public ResponseEntity<ResponseBody<RecruitmentBoardInfoAndFormResponse>> getDraftRecruitmentBoard(
            Long userId) {
        return ResponseEntity.ok(createSuccessResponse(recruitmentBoardService.getLatestDraftBoardAndForm(userId)));
    }

    /**
     * 사용자의 임시저장 게시물 목록 get(No-Offset)
     */
    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_ACTIVE_USER')")
    @GetMapping("/draft")
    public ResponseEntity<ResponseBody<RecruitmentBoardNoOffsetResponse>> getDraftRecruitmentBoardList(
            Long userId,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long lastBoardId) {
        return ResponseEntity.ok(createSuccessResponse(recruitmentBoardService.getDraftBoardListByUserId(userId, size, lastBoardId)));
    }

    /**
     * 사용자가 작성한 글 리스트 조회(PageNum)
     *
     * @param : size, page, boardType[study, project, mentoring]
     */
    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_ACTIVE_USER')")
    @GetMapping("/my-boards")
    public ResponseEntity<ResponseBody<RecruitmentBoardPageNumResponse>> getPublishedUserRecruitmentBoardList(
            Long userId,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam RecruitmentBoardType boardType) {
        return ResponseEntity.ok(createSuccessResponse(
                recruitmentBoardService.getPublishedBoardListByUserId(userId, pageable, boardType)));
    }

    // Valid 검사 메서드
    // 임시저장은 valid 검사를 하지 않음
    private void validateRecruitmentBoardInfoAndFormRequest(Status status, @Valid RecruitmentBoardInfoAndFormRequest request) throws MethodArgumentNotValidException {
        switch (status) {
            case PUBLISHED -> {
                Set<ConstraintViolation<RecruitmentBoardInfoAndFormRequest>> violations = validator.validate(request);
                if (!violations.isEmpty()) {
                    BindingResult bindingResult = new BeanPropertyBindingResult(request, "recruitmentBoardInfoAndFormRequest");
                    for (ConstraintViolation<RecruitmentBoardInfoAndFormRequest> violation : violations) {
                        bindingResult.addError(new ObjectError("recruitmentBoardInfoAndFormRequest", violation.getMessage()));
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
