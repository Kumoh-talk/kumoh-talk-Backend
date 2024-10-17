package com.example.demo.domain.report.service;

import com.example.demo.domain.comment.domain.entity.Comment;
import com.example.demo.domain.comment.repository.CommentRepository;
import com.example.demo.domain.report.client.DiscordReportClient;
import com.example.demo.domain.report.client.DiscordMessage;
import com.example.demo.domain.report.domain.Report;
import com.example.demo.domain.report.domain.dto.ReportResponse;
import com.example.demo.domain.report.repository.ReportRepository;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final DiscordReportClient discordReportClient;

    @Transactional
    public void report(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new ServiceException(ErrorCode.COMMENT_NOT_FOUND));
        User user = userRepository.findById(userId).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));

        Report report = Report.from(user, comment);
        reportRepository.save(report);
        this.sendDiscordAlarm(user, comment);
    }

    private void sendDiscordAlarm(User user, Comment comment) {
        discordReportClient.sendReport(DiscordMessage.createCommentReportMessage(user, comment));
    }

    public Page<ReportResponse> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Report> reportPage = reportRepository.findAllByOrderByCreatedAtDesc(pageable);
        return reportPage.map(ReportResponse::from);
    }
}
