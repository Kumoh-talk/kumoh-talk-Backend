package com.example.demo.domain.report.repository;

import com.example.demo.domain.report.domain.ReportInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportRepository {

    void saveBoardCommentReport(Long userId, Long commentId);

    void saveRecruitmentBoardCommentReport(Long userId, Long commentId);

    boolean existsBoardCommentReport(Long userId, Long commentId);

    boolean existsRecruitmentBoardCommentReport(Long userId, Long commentId);

    Page<ReportInfo> findAll(Pageable pageable);

}
