package com.example.demo.domain.recruitment_board.repository;

import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentFormQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecruitmentFormQuestionRepository extends JpaRepository<RecruitmentFormQuestion, Long>, QueryDslRecruitmentFormQuestionRepository {
    @Modifying
    @Query("DELETE FROM RecruitmentFormQuestion q WHERE q.id IN :ids")
    void hardDeleteQuestionsByIds(List<Long> ids);

    @Modifying
    @Query("UPDATE RecruitmentFormQuestion q SET q.deletedAt = NOW() WHERE q.id IN :ids")
    void softDeleteQuestionsByIds(List<Long> ids);
}
