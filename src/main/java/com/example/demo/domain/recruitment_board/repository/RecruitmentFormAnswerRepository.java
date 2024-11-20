package com.example.demo.domain.recruitment_board.repository;

import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentFormAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RecruitmentFormAnswerRepository extends JpaRepository<RecruitmentFormAnswer, Long> {
    @Modifying
    @Query(value = "DELETE FROM recruitment_form_answers c WHERE c.id IN :ids", nativeQuery = true)
    void hardDeleteAnswersByIds(List<Long> ids);

    Optional<RecruitmentFormAnswer> findByRecruitmentFormQuestion_IdAndNumber(Long recruitmentFormQuestionId, Integer number);
}
