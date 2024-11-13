package com.example.demo.domain.report.controller;

import com.example.demo.domain.report.domain.dto.ReportResponse;
import com.example.demo.domain.report.service.ReportService;
import com.example.demo.global.base.dto.ResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.global.base.dto.ResponseUtil.createSuccessResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/admin/reports")
public class ReportAdminController {

    private final ReportService reportService;

    /**
     * 관리자 기능 - 모든 신고목록 보기
     */
    @GetMapping
    public ResponseEntity<ResponseBody<Page<ReportResponse>>> getAllReport(
            @PageableDefault(page=0, size=10,sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(createSuccessResponse(reportService.getAllReport(pageable)));
    }
}
