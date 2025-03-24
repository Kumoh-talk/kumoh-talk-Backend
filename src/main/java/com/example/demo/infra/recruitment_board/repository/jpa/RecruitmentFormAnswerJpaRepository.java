package com.example.demo.infra.recruitment_board.repository.jpa;

import com.example.demo.infra.recruitment_board.entity.RecruitmentFormAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RecruitmentFormAnswerJpaRepository extends JpaRepository<RecruitmentFormAnswer, Long> {
    @Modifying
    @Query(value = "DELETE FROM recruitment_form_answers c WHERE c.id IN :ids", nativeQuery = true)
    void hardDeleteAnswersByIds(List<Long> ids);

    Optional<RecruitmentFormAnswer> findByRecruitmentFormQuestionIdAndNumber(Long recruitmentFormQuestionId, Integer number);
}
