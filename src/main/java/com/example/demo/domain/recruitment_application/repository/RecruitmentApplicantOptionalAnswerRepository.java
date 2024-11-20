package com.example.demo.domain.recruitment_application.repository;

import com.example.demo.domain.recruitment_application.domain.entity.RecruitmentApplicantOptionalAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecruitmentApplicantOptionalAnswerRepository extends JpaRepository<RecruitmentApplicantOptionalAnswer, Long> {
    @Query("SELECT a FROM RecruitmentApplicantOptionalAnswer a " +
            "JOIN FETCH a.recruitmentFormAnswer " +
            "JOIN FETCH a.recruitmentFormAnswer.recruitmentFormQuestion " +
            "WHERE a.recruitmentApplicant.id = :applicantId")
    List<RecruitmentApplicantOptionalAnswer> findByRecruitmentApplicant_IdFetchAnswerAndQuestion(Long applicantId);

    @Modifying
    @Query(value = "DELETE FROM recruitment_applicants_optional_answers a WHERE a.id IN :ids", nativeQuery = true)
    void hardDeleteAnswersByIds(List<Long> ids);
}
