package com.example.demo.domain.report.controller;

import com.example.demo.domain.report.service.ReportService;
import com.example.demo.global.aop.AssignUserId;
import com.example.demo.global.base.dto.ResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.global.base.dto.ResponseUtil.createSuccessResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final ReportService reportService;

    /**
     * 신고하기 - comment
     */
    @AssignUserId
    @PostMapping("/comments/{commentId}")
    public ResponseEntity<ResponseBody<Void>> report(@PathVariable Long commentId,
                                                     Long userId) {
        reportService.report(commentId, userId);
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
