package com.example.demo.domain.recruitment_board.repository;

import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentFormQuestion;

import java.util.List;
import java.util.Optional;

public interface QueryDslRecruitmentFormQuestionRepository {
    Optional<List<RecruitmentFormQuestion>> findByBoard_IdByFetchingAnswerList(Long recruitmentBoardId);
}
