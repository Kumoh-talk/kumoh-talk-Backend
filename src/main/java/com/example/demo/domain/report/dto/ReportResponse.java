package com.example.demo.domain.report.dto;

import com.example.demo.domain.comment.domain.response.CommentInfo;
import com.example.demo.domain.report.domain.Report;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ReportResponse(
        String nickname,
        CommentInfo commentInfo,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt
) {
    public static ReportResponse from(Report report) { // TODO. 프론트 협의 & N+1 생각
        return new ReportResponse(
                report.getUser().getNickname(),
                CommentInfo.from(report.getComment()),
                report.getCreatedAt()
        );
    }
}
