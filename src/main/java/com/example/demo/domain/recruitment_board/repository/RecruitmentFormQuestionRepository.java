package com.example.demo.domain.recruitment_board.repository;

import com.example.demo.domain.recruitment_board.entity.RecruitmentFormQuestionInfo;

import java.util.List;

public interface RecruitmentFormQuestionRepository {
    List<RecruitmentFormQuestionInfo> getByBoardIdWithAnswerList(Long recruitmentBoardId);

    List<Long> getEssentialListByRecruitmentBoardId(Long boardId);

}
