package com.example.demo.infra.report.repository;

import com.example.demo.domain.user.domain.User;
import com.example.demo.infra.comment.entity.BoardComment;
import com.example.demo.infra.comment.entity.RecruitmentBoardComment;
import com.example.demo.infra.report.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportJpaRepository extends JpaRepository<Report, Long> {
    boolean existsByUserAndBoardComment(User user, BoardComment comment);

    boolean existsByUserAndRecruitmentBoardComment(User user, RecruitmentBoardComment comment);

    @Query("SELECT r FROM Report r " +
            "JOIN FETCH r.user " +
            "LEFT JOIN FETCH r.boardComment " +
            "LEFT JOIN FETCH r.recruitmentBoardComment")
    Page<Report> findAllFetchJoin(Pageable pageable);
}
