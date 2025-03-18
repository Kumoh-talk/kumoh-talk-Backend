package com.example.demo.domain.report.domain;

import com.example.demo.domain.user.entity.UserTarget;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReportInfo {
    private final Long reportId;
    private final ReportedCommentInfo commentInfo;
    private final UserTarget userTarget;
    private final LocalDateTime createdAt;

    @Builder
    public ReportInfo(Long reportId, ReportedCommentInfo commentInfo, UserTarget userTarget, LocalDateTime createdAt) {
        this.reportId = reportId;
        this.commentInfo = commentInfo;
        this.userTarget = userTarget;
        this.createdAt = createdAt;
    }
}
