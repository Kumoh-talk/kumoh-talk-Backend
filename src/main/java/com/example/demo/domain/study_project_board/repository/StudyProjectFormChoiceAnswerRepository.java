package com.example.demo.domain.study_project_board.repository;

import com.example.demo.domain.study_project_board.domain.entity.StudyProjectFormChoiceAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudyProjectFormChoiceAnswerRepository extends JpaRepository<StudyProjectFormChoiceAnswer, Long> {
    @Modifying
    @Query("DELETE FROM StudyProjectFormChoiceAnswer c WHERE c.id IN :ids")
    void hardDeleteAnswersByIds(List<Long> ids);

    @Modifying
    @Query("UPDATE StudyProjectFormChoiceAnswer a SET a.deletedAt = NOW() WHERE a.id IN :ids")
    void softDeleteAnswersByIds(List<Long> ids);
}
