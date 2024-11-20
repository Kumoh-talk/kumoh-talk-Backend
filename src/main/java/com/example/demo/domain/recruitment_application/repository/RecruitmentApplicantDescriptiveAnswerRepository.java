package com.example.demo.domain.recruitment_application.repository;

import com.example.demo.domain.recruitment_application.domain.entity.RecruitmentApplicantDescriptiveAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecruitmentApplicantDescriptiveAnswerRepository extends JpaRepository<RecruitmentApplicantDescriptiveAnswer, Long> {

    @Query("SELECT a FROM RecruitmentApplicantDescriptiveAnswer a " +
            "JOIN FETCH a.recruitmentFormQuestion " +
            "WHERE a.recruitmentApplicant.id = :applicantId")
    List<RecruitmentApplicantDescriptiveAnswer> findByRecruitmentApplicant_IdFetchQuestion(Long applicantId);

    @Modifying
    @Query(value = "DELETE FROM recruitment_applicants_descriptive_answers a WHERE a.id IN :ids", nativeQuery = true)
    void hardDeleteAnswersByIds(List<Long> ids);
}
