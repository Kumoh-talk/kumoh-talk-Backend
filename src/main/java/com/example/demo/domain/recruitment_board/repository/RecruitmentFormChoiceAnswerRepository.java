package com.example.demo.domain.recruitment_board.repository;

import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentFormChoiceAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecruitmentFormChoiceAnswerRepository extends JpaRepository<RecruitmentFormChoiceAnswer, Long> {
    @Modifying
    @Query("DELETE FROM RecruitmentFormChoiceAnswer c WHERE c.id IN :ids")
    void hardDeleteAnswersByIds(List<Long> ids);

    @Modifying
    @Query("UPDATE RecruitmentFormChoiceAnswer a SET a.deletedAt = NOW() WHERE a.id IN :ids")
    void softDeleteAnswersByIds(List<Long> ids);
}
