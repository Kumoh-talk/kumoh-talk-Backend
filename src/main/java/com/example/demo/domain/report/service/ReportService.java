package com.example.demo.domain.report.service;

import com.example.demo.domain.comment.entity.CommentInfo;
import com.example.demo.domain.comment.implement.comment.BoardCommentHandler;
import com.example.demo.domain.comment.implement.comment.RecruitmentBoardCommentHandler;
import com.example.demo.domain.report.client.DiscordMessage;
import com.example.demo.domain.report.client.DiscordReportClient;
import com.example.demo.domain.report.domain.ReportInfo;
import com.example.demo.domain.report.implement.ReportReader;
import com.example.demo.domain.report.implement.ReportWriter;
import com.example.demo.domain.user.entity.UserTarget;
import com.example.demo.domain.user.implement.UserReader;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ReportService {

    private final ReportReader reportReader;
    private final ReportWriter reportWriter;
    private final DiscordReportClient discordReportClient;

    private final UserReader userReader;
    private final BoardCommentHandler boardCommentHandler;
    private final RecruitmentBoardCommentHandler recruitmentBoardCommentHandler;

    @Transactional
    public void reportBoardComment(Long commentId, Long userId) {
        UserTarget userTarget = userReader.findUserTarget(userId)
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
        CommentInfo commentInfo = boardCommentHandler.getById(commentId)
                .orElseThrow(() -> new ServiceException(ErrorCode.COMMENT_NOT_FOUND));

        reportReader.validateBoardComment(userTarget, commentInfo);
        reportWriter.reportBoardComment(userTarget, commentInfo);

        sendDiscordAlarm(userTarget, commentInfo);
    }

    @Transactional
    public void reportRecruitmentBoardComment(Long commentId, Long userId) {
        UserTarget userTarget = userReader.findUserTarget(userId)
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
        CommentInfo commentInfo = recruitmentBoardCommentHandler.getById(commentId)
                .orElseThrow(() -> new ServiceException(ErrorCode.COMMENT_NOT_FOUND));

        reportReader.validateRecruitmentBoardComment(userTarget, commentInfo);
        reportWriter.reportRecruitmentBoardComment(userTarget, commentInfo);

        sendDiscordAlarm(userTarget, commentInfo);
    }

    private void sendDiscordAlarm(UserTarget userTarget, CommentInfo commentInfo) {
        discordReportClient.sendReport(DiscordMessage.createCommentReportMessage(userTarget, commentInfo));
    }

    public Page<ReportInfo> getAllReport(Pageable pageable) {
        return reportReader.getAllReport(pageable);
    }
}
