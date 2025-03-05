package com.example.demo.domain.recruitment_board.entity;

import com.example.demo.domain.recruitment_board.entity.vo.QuestionType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class RecruitmentFormQuestionInfo {
    private Long questionId;
    private Integer number;
    private String question;
    private QuestionType type;
    private Boolean isEssential;
    private List<RecruitmentFormAnswerInfo> answerList;

    @Builder
    public RecruitmentFormQuestionInfo(Long questionId, Integer number, String question, QuestionType type, Boolean isEssential, List<RecruitmentFormAnswerInfo> answerList) {
        this.questionId = questionId;
        this.number = number;
        this.question = question;
        this.type = type;
        this.isEssential = isEssential;
        this.answerList = answerList;
    }
}
