package com.example.demo.domain.recruitment_application.repository;

import com.example.demo.domain.recruitment_application.domain.entity.RecruitmentApplicant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitmentApplicantRepository extends JpaRepository<RecruitmentApplicant, Long>, QueryDslRecruitmentApplicantRepository {
    boolean existsByRecruitmentBoard_Id(Long recruitmentBoardId);

    boolean existsByUser_IdAndRecruitmentBoard_Id(Long userId, Long recruitmentBoardId);

    Page<RecruitmentApplicant> findByRecruitmentBoard_IdOrderByCreatedAtDesc(Pageable pageable, Long recruitmentBoardId);
}
