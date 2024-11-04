package com.example.demo.domain.recruitment_board.controller;

import com.example.demo.domain.recruitment_board.service.RecruitmentBoardService;
import com.example.demo.global.base.dto.ResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.demo.global.base.dto.ResponseUtil.createSuccessResponse;

@RestController
@RequestMapping("/api/v1/admin/recruitment-boards")
@RequiredArgsConstructor
public class AdminRecruitmentBoardController {
    private final RecruitmentBoardService recruitmentBoardService;

    /**
     * 관리자 전용 스터디, 프로젝트, 멘토링 게시물 및 신청폼 삭제 API
     */
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping("/{recruitmentBoardId}")
    public ResponseEntity<ResponseBody<Void>> deleteRecruitmentBoardAndFormByAdmin(
            @PathVariable Long recruitmentBoardId) {
        recruitmentBoardService.deleteBoardAndForm(null, recruitmentBoardId, true);
        return ResponseEntity.ok(createSuccessResponse());
    }
}
