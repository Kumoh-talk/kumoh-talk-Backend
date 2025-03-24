package com.example.demo.application.report.controller;

import com.example.demo.application.report.api.ReportAdminApi;
import com.example.demo.application.report.dto.response.ReportResponse;
import com.example.demo.domain.report.service.ReportService;
import com.example.demo.global.base.dto.ResponseBody;
import com.example.demo.global.base.dto.page.GlobalPageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.global.base.dto.ResponseUtil.createSuccessResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/admin/reports")
public class ReportAdminController implements ReportAdminApi {

    private final ReportService reportService;

    /**
     * 관리자 기능 - 모든 신고목록 보기
     */
    @GetMapping
    public ResponseEntity<ResponseBody<GlobalPageResponse<ReportResponse>>> getAllReport(
            @PageableDefault(page=0, size=10,sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(createSuccessResponse(GlobalPageResponse.create(
                reportService.getAllReport(pageable).map(ReportResponse::from)
        )));
    }
}
