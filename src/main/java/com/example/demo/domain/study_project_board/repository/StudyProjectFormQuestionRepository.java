package com.example.demo.domain.study_project_board.repository;

import com.example.demo.domain.study_project_board.domain.entity.StudyProjectFormQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudyProjectFormQuestionRepository extends JpaRepository<StudyProjectFormQuestion, Long>, QueryDslStudyProjectFormQuestionRepository {
    @Modifying
    @Query("DELETE FROM StudyProjectFormQuestion q WHERE q.id IN :ids")
    void hardDeleteQuestionsByIds(List<Long> ids);

    @Modifying
    @Query("UPDATE StudyProjectFormQuestion q SET q.deletedAt = NOW() WHERE q.id IN :ids")
    void softDeleteQuestionsByIds(List<Long> ids);
}
