package com.example.demo.domain.recruitment_application.repository;

import com.example.demo.domain.recruitment_application.domain.entity.RecruitmentApplicantAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RecruitmentApplicantAnswerRepository extends JpaRepository<RecruitmentApplicantAnswer, Long> {

    @Query("SELECT a FROM RecruitmentApplicantAnswer a " +
            "JOIN FETCH a.recruitmentFormQuestion " +
            "WHERE a.recruitmentApplicant.id = :applicantId")
    Optional<List<RecruitmentApplicantAnswer>> findByRecruitmentApplicant_IdFetchQuestion(Long applicantId);
}
