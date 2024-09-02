package com.example.demo.domain.study_project_board.repository;

import com.example.demo.domain.study_project_board.domain.entity.StudyProjectFormQuestion;

import java.util.List;
import java.util.Optional;

public interface QueryDslStudyProjectFormQuestionRepository {
    Optional<List<StudyProjectFormQuestion>> findByBoard_IdByFetchingAnswerList(Long applicationFormQuestionId);
}
