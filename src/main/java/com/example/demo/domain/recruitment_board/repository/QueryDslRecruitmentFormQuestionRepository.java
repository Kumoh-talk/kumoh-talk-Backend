package com.example.demo.domain.recruitment_board.repository;

import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentFormQuestion;

import java.util.List;

public interface QueryDslRecruitmentFormQuestionRepository {
    List<RecruitmentFormQuestion> findByBoard_IdByFetchingAnswerList(Long recruitmentBoardId);
}
