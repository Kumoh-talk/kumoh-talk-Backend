package com.example.demo.infra.report.repository;

import com.example.demo.domain.comment.entity.CommentUserInfo;
import com.example.demo.domain.report.domain.ReportInfo;
import com.example.demo.domain.report.domain.ReportedCommentInfo;
import com.example.demo.domain.report.repository.ReportRepository;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.UserTarget;
import com.example.demo.domain.user.repository.UserJpaRepository;
import com.example.demo.infra.comment.entity.BoardComment;
import com.example.demo.infra.comment.entity.RecruitmentBoardComment;
import com.example.demo.infra.comment.repository.jpa.BoardCommentJpaRepository;
import com.example.demo.infra.comment.repository.jpa.RecruitmentBoardCommentJpaRepository;
import com.example.demo.infra.report.entity.Report;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class ReportRepositoryImpl implements ReportRepository {
    private final ReportJpaRepository reportJpaRepository;

    private final UserJpaRepository userJpaRepository;
    private final BoardCommentJpaRepository boardCommentJpaRepository;
    private final RecruitmentBoardCommentJpaRepository recruitmentBoardCommentJpaRepository;

    @Override
    @Transactional
    public void saveBoardCommentReport(Long userId, Long commentId) {
        User user = userJpaRepository.findById(userId).get();
        BoardComment comment = (BoardComment) boardCommentJpaRepository.doFindById(commentId).get();

        Report report = Report.fromBoardComment(user, comment);
        reportJpaRepository.save(report);
    }

    @Override
    @Transactional
    public void saveRecruitmentBoardCommentReport(Long userId, Long commentId) {
        User user = userJpaRepository.findById(userId).get();
        RecruitmentBoardComment comment = (RecruitmentBoardComment) recruitmentBoardCommentJpaRepository.doFindById(commentId).get();

        Report report = Report.fromRecruitmentBoardComment(user, comment);
        reportJpaRepository.save(report);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsBoardCommentReport(Long userId, Long commentId) {
        User user = userJpaRepository.findById(userId).get();
        BoardComment comment = (BoardComment) boardCommentJpaRepository.doFindById(commentId).get();

        return reportJpaRepository.existsByUserAndBoardComment(user, comment);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsRecruitmentBoardCommentReport(Long userId, Long commentId) {
        User user = userJpaRepository.findById(userId).get();
        RecruitmentBoardComment comment = (RecruitmentBoardComment) recruitmentBoardCommentJpaRepository.doFindById(commentId).get();

        return reportJpaRepository.existsByUserAndRecruitmentBoardComment(user, comment);
    }

    @Override
    public Page<ReportInfo> findAll(Pageable pageable) {
        return reportJpaRepository.findAllFetchJoin(pageable).map(report -> ReportInfo.builder()
                .reportId(report.getId())
                .commentInfo(report.getBoardComment() != null
                        ? ReportedCommentInfo.builder()
                                .commentId(report.getBoardComment().getId())
                                .commentUserInfo(CommentUserInfo.builder()
                                        .userId(report.getBoardComment().getUser().getId())
                                        .nickName(report.getBoardComment().getUser().getNickname())
                                        .userRole(report.getBoardComment().getUser().getRole())
                                        .profileImageUrl(report.getBoardComment().getUser().getProfileImageUrl())
                                        .build())
                                .content(report.getBoardComment().getContent())
                                .createdAt(report.getBoardComment().getCreatedAt())
                                .updatedAt(report.getBoardComment().getUpdatedAt())
                                .deletedAt(report.getBoardComment().getDeletedAt())
                                .build()
                        : ReportedCommentInfo.builder()
                                .commentId(report.getRecruitmentBoardComment().getId())
                                .commentUserInfo(CommentUserInfo.builder()
                                        .userId(report.getRecruitmentBoardComment().getUser().getId())
                                        .nickName(report.getRecruitmentBoardComment().getUser().getNickname())
                                        .userRole(report.getRecruitmentBoardComment().getUser().getRole())
                                        .profileImageUrl(report.getRecruitmentBoardComment().getUser().getProfileImageUrl())
                                        .build())
                                .content(report.getRecruitmentBoardComment().getContent())
                                .createdAt(report.getRecruitmentBoardComment().getCreatedAt())
                                .updatedAt(report.getRecruitmentBoardComment().getUpdatedAt())
                                .deletedAt(report.getRecruitmentBoardComment().getDeletedAt())
                                .build())
                .userTarget(UserTarget.builder()
                    .userId(report.getUser().getId())
                    .userRole(report.getUser().getRole())
                    .nickName(report.getUser().getNickname())
                    .build())
                .createdAt(report.getCreatedAt())
                .build());
    }
}
