package com.example.demo.infra.recruitment_application.repository.jpa;

import com.example.demo.infra.recruitment_application.entity.RecruitmentApplicationOptionalAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecruitmentApplicationOptionalAnswerJpaRepository extends JpaRepository<RecruitmentApplicationOptionalAnswer, Long> {
    @Query("SELECT a FROM RecruitmentApplicationOptionalAnswer a " +
            "JOIN FETCH a.recruitmentFormAnswer " +
            "JOIN FETCH a.recruitmentFormAnswer.recruitmentFormQuestion " +
            "WHERE a.recruitmentApplication.id = :applicationId")
    List<RecruitmentApplicationOptionalAnswer> findByRecruitmentApplicationIdWithAnswerAndQuestion(Long applicationId);

    @Modifying
    @Query(value = "DELETE FROM recruitment_application_optional_answers a WHERE a.id IN :ids", nativeQuery = true)
    void hardDeleteAnswersByIds(List<Long> ids);
}
