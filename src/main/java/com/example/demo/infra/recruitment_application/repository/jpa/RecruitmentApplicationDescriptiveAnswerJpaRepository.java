package com.example.demo.infra.recruitment_application.repository.jpa;

import com.example.demo.infra.recruitment_application.entity.RecruitmentApplicationDescriptiveAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecruitmentApplicationDescriptiveAnswerJpaRepository extends JpaRepository<RecruitmentApplicationDescriptiveAnswer, Long> {

    @Query("SELECT a FROM RecruitmentApplicationDescriptiveAnswer a " +
            "JOIN FETCH a.recruitmentFormQuestion " +
            "WHERE a.recruitmentApplication.id = :applicantId")
    List<RecruitmentApplicationDescriptiveAnswer> findByRecruitmentApplicationIdWithQuestion(Long applicantId);

    @Modifying
    @Query(value = "DELETE FROM recruitment_application_descriptive_answers a WHERE a.id IN :ids", nativeQuery = true)
    void hardDeleteAnswersByIds(List<Long> ids);
}
