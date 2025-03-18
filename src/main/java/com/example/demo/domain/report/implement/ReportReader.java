package com.example.demo.domain.report.implement;

import com.example.demo.domain.comment.entity.CommentInfo;
import com.example.demo.domain.report.domain.ReportInfo;
import com.example.demo.domain.report.repository.ReportRepository;
import com.example.demo.domain.user.entity.UserTarget;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ReportReader {
    private final ReportRepository reportRepository;

    @Transactional(readOnly = true)
    public void validateBoardComment(UserTarget userTarget, CommentInfo commentInfo) {
        if (reportRepository.existsBoardCommentReport(userTarget.getUserId(), commentInfo.getCommentId())) {
            throw new ServiceException(ErrorCode.USER_ALREADY_REPORT_COMMENT);
        }
    }

    @Transactional(readOnly = true)
    public void validateRecruitmentBoardComment(UserTarget userTarget, CommentInfo commentInfo) {
        if (reportRepository.existsRecruitmentBoardCommentReport(userTarget.getUserId(), commentInfo.getCommentId())) {
            throw new ServiceException(ErrorCode.USER_ALREADY_REPORT_COMMENT);
        }
    }

    public Page<ReportInfo> getAllReport(Pageable pageable) {
        return reportRepository.findAll(pageable);
    }
}
