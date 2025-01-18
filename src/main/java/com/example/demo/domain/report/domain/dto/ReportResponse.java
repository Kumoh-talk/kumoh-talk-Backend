package com.example.demo.domain.report.domain.dto;

import com.example.demo.domain.comment.domain.dto.response.CommentInfoResponse;
import com.example.demo.domain.report.domain.Report;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema
public record ReportResponse(
        String nickname,
        CommentInfoResponse commentInfoResponse,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt
) {
    public static ReportResponse from(Report report) { // TODO. 프론트 협의 & N+1 생각
        if (report.getBoardComment() == null) {
            return new ReportResponse(
                    report.getUser().getNickname(),
                    CommentInfoResponse.fromComment(report.getRecruitmentBoardComment()),
                    report.getCreatedAt()
            );
        } else {
            return new ReportResponse(
                    report.getUser().getNickname(),
                    CommentInfoResponse.fromComment(report.getBoardComment()),
                    report.getCreatedAt()
            );
        }
    }
}
