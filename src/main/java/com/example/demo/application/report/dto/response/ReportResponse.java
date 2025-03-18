package com.example.demo.application.report.dto.response;

import com.example.demo.domain.report.domain.ReportInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema
public record ReportResponse(
        String nickname,
        ReportedCommentInfoResponse commentInfoResponse,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt
) {
    public static ReportResponse from(ReportInfo reportInfo) {
        return new ReportResponse(
                reportInfo.getUserTarget().getNickName(),
                ReportedCommentInfoResponse.from(reportInfo.getCommentInfo()),
                reportInfo.getCreatedAt()
        );
    }
}
