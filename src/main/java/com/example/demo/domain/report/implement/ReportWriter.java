package com.example.demo.domain.report.implement;

import com.example.demo.domain.comment.entity.CommentInfo;
import com.example.demo.domain.report.repository.ReportRepository;
import com.example.demo.domain.user.entity.UserTarget;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportWriter {
    private final ReportRepository reportRepository;

    public void reportBoardComment(UserTarget userTarget, CommentInfo commentInfo) {
        reportRepository.saveBoardCommentReport(userTarget.getUserId(), commentInfo.getCommentId());
    }

    public void reportRecruitmentBoardComment(UserTarget userTarget, CommentInfo commentInfo) {
        reportRepository.saveRecruitmentBoardCommentReport(userTarget.getUserId(), commentInfo.getCommentId());
    }
}
