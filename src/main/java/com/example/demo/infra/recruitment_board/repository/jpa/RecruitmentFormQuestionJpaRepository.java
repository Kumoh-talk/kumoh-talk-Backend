package com.example.demo.infra.recruitment_board.repository.jpa;

import com.example.demo.infra.recruitment_board.entity.RecruitmentFormQuestion;
import com.example.demo.infra.recruitment_board.repository.querydsl.QueryDslRecruitmentFormQuestionRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecruitmentFormQuestionJpaRepository extends JpaRepository<RecruitmentFormQuestion, Long>, QueryDslRecruitmentFormQuestionRepository {
    @Modifying
    @Query(value = "DELETE FROM recruitment_form_questions q WHERE q.id IN :ids", nativeQuery = true)
    void hardDeleteQuestionsByIds(List<Long> ids);
}
