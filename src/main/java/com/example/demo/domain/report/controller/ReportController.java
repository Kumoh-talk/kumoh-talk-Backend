package com.example.demo.domain.report.controller;

import com.example.demo.domain.report.api.ReportApi;
import com.example.demo.domain.report.service.ReportService;
import com.example.demo.global.aop.AssignUserId;
import com.example.demo.global.base.dto.ResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.demo.global.base.dto.ResponseUtil.createSuccessResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/reports")
public class ReportController implements ReportApi {

    private final ReportService reportService;

    /**
     * 신고하기 - BoardComment
     */
    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    @PostMapping("/board/comments/{commentId}")
    public ResponseEntity<ResponseBody<Void>> reportBoardComment(@PathVariable Long commentId,
                                                                 Long userId) {
        reportService.reportBoardComment(commentId, userId);
        return ResponseEntity.ok(createSuccessResponse());
    }

    /**
     * 신고하기 - RecruitmentBoardComment
     */
    @AssignUserId
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    @PostMapping("/recruitment-board/comments/{commentId}")
    public ResponseEntity<ResponseBody<Void>> reportRecruitmentBoardComment(@PathVariable Long commentId,
                                                                            Long userId) {
        reportService.reportRecruitmentBoardComment(commentId, userId);
        return ResponseEntity.ok(createSuccessResponse());
    }

//    /**
//     * 관리자 기능 - 모든 신고목록 보기
//     */
//    @GetMapping
//    public ResponseEntity<ResponseBody<Page<ReportResponse>>> findAll(@RequestParam(defaultValue = "0") int page,
//                                                                      @RequestParam(defaultValue = "10") int size) {
//
//        return ResponseEntity.ok(createSuccessResponse(reportService.findAll(page,size)));
//    }
}
