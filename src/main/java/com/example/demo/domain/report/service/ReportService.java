package com.example.demo.domain.report.service;

import com.example.demo.domain.report.client.DiscordMessage;
import com.example.demo.domain.report.client.DiscordReportClient;
import com.example.demo.domain.report.domain.Report;
import com.example.demo.domain.report.domain.dto.ReportResponse;
import com.example.demo.domain.report.repository.ReportRepository;
import com.example.demo.infra.user.entity.User;
import com.example.demo.infra.user.repository.UserJpaRepository;
import com.example.demo.global.base.dto.page.GlobalPageResponse;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.infra.comment.entity.Comment;
import com.example.demo.infra.comment.repository.jpa.BoardCommentJpaRepository;
import com.example.demo.infra.comment.repository.jpa.RecruitmentBoardCommentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserJpaRepository userJpaRepository;
    private final BoardCommentJpaRepository boardCommentRepository;
    private final RecruitmentBoardCommentJpaRepository recruitmentBoardCommentRepository;
    private final DiscordReportClient discordReportClient;

    @Transactional
    public void reportBoardComment(Long commentId, Long userId) {
        Comment comment = boardCommentRepository.findById(commentId).orElseThrow(() ->
                new ServiceException(ErrorCode.COMMENT_NOT_FOUND));
        saveCommentReport(userId, comment);
    }

    @Transactional
    public void reportRecruitmentBoardComment(Long commentId, Long userId) {
        Comment comment = recruitmentBoardCommentRepository.findById(commentId).orElseThrow(() ->
                new ServiceException(ErrorCode.COMMENT_NOT_FOUND));
        saveCommentReport(userId, comment);
    }

    private void saveCommentReport(Long userId, Comment comment) {
        User user = userJpaRepository.findById(userId).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));

        Report report = comment.toReport(user);
        reportRepository.save(report);
        this.sendDiscordAlarm(user, comment);
    }

    private void sendDiscordAlarm(User user, Comment comment) {
        discordReportClient.sendReport(DiscordMessage.createCommentReportMessage(user, comment));
    }

    public GlobalPageResponse<ReportResponse> getAllReport(Pageable pageable) {
        Page<ReportResponse> pages = reportRepository.findAll(pageable).map(ReportResponse::from);
        return GlobalPageResponse.create(pages);
    }
}
