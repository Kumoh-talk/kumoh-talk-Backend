package com.example.demo.domain.recruitment_board.entity;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class RecruitmentBoardAndFormInfo {
    private RecruitmentBoardInfo board;
    private List<RecruitmentFormQuestionInfo> form;

    @Builder
    public RecruitmentBoardAndFormInfo(RecruitmentBoardInfo board, List<RecruitmentFormQuestionInfo> form) {
        this.board = board;
        this.form = form;
    }

    public static RecruitmentBoardAndFormInfo of(RecruitmentBoardInfo board, List<RecruitmentFormQuestionInfo> form) {
        return RecruitmentBoardAndFormInfo.builder()
                .board(board)
                .form(form)
                .build();
    }
}
