package com.example.demo.application.recruitment_board.controller;

import com.example.demo.application.recruitment_board.api.AdminRecruitmentBoardApi;
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
public class AdminRecruitmentBoardController implements AdminRecruitmentBoardApi {
    private final RecruitmentBoardService recruitmentBoardService;

    /**
     * [관리자 전용 모집 게시물 및 신청폼 삭제] <br>
     * 모집 게시물 및 신청폼, 신청자, 신청서, 댓글을 OrphanRemoval 설정을 통해 Soft Delete
     *
     * @apiNote 1. SoftDelete 설정을 사용할거면 통일하는게 좋다고 생각하여 해당 도메인 관련 모든 엔티티에 Soft Delete 적용 <br>
     * -> OrpahnRemoval을 통해 삭제가 될 때 Hard Delete, Soft Delete가 섞이면 곤란할 것이라고 생각했기 때문 <br>
     * 2. 필터에서 유저 권한이 ADMIN인 것을 확인하면 따로 서비스 로직에서 유저 인증을 거치지 않도록 isAuthorized 매개변수를 true로 하여 서비스 메서드를 호출
     */
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping("/{recruitmentBoardId}")
    public ResponseEntity<ResponseBody<Void>> deleteRecruitmentBoardAndFormByAdmin(
            @PathVariable Long recruitmentBoardId) {
        recruitmentBoardService.deleteBoardAndForm(null, recruitmentBoardId, true);
        return ResponseEntity.ok(createSuccessResponse());
    }
}
