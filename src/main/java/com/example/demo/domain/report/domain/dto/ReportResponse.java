package com.example.demo.domain.report.domain.dto;

import com.example.demo.domain.comment.domain.response.CommentInfoResponse;
import com.example.demo.domain.report.domain.Report;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ReportResponse(
        String nickname,
        CommentInfoResponse commentInfoResponse,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt
) {
    public static ReportResponse from(Report report) { // TODO. 프론트 협의 & N+1 생각
        return new ReportResponse(
                report.getUser().getNickname(),
                CommentInfoResponse.from(report.getComment()),
                report.getCreatedAt()
        );
    }
}
