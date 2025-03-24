package com.example.demo.infra.recruitment_board.repository.querydsl;

import com.example.demo.infra.recruitment_board.entity.RecruitmentFormQuestion;

import java.util.List;

public interface QueryDslRecruitmentFormQuestionRepository {
    List<RecruitmentFormQuestion> findByBoardIdWithAnswerList(Long recruitmentBoardId);

    List<Long> findEssentialListByRecruitmentBoardId(Long boardId);
}
