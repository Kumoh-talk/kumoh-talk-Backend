package com.example.demo.infra.recruitment_application.repository.jpa;

import com.example.demo.infra.recruitment_application.entity.RecruitmentApplication;
import com.example.demo.infra.recruitment_application.repository.querydsl.QueryDslRecruitmentApplicationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RecruitmentApplicationJpaRepository extends JpaRepository<RecruitmentApplication, Long>, QueryDslRecruitmentApplicationRepository {
    boolean existsByRecruitmentBoardId(Long recruitmentBoardId);

    boolean existsByUserIdAndRecruitmentBoardId(Long userId, Long recruitmentBoardId);

    @Query("SELECT ra FROM RecruitmentApplication ra " +
            "JOIN FETCH ra.recruitmentBoard " +
            "JOIN FETCH ra.user " +
            "WHERE ra.id = :id")
    Optional<RecruitmentApplication> findByIdWithUserAndBoard(Long id);

    @Query("SELECT ra FROM RecruitmentApplication ra " +
            "JOIN FETCH ra.user " +
            "WHERE ra.id = :id")
    Optional<RecruitmentApplication> findByIdWithUser(Long id);

    @Query("SELECT ra FROM RecruitmentApplication ra " +
            "JOIN FETCH ra.user " +
            "WHERE ra.recruitmentBoard.id = :recruitmentBoardId " +
            "ORDER BY ra.createdAt DESC")
    Page<RecruitmentApplication> findPageByRecruitmentBoardIdOrderByCreatedAtDesc(Pageable pageable, Long recruitmentBoardId);
}
