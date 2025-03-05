package com.example.demo.domain.recruitment_board.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RecruitmentFormAnswerInfo {
    private Long answerId;
    private Integer number;
    private String answer;

    @Builder
    public RecruitmentFormAnswerInfo(Long answerId, Integer number, String answer) {
        this.answerId = answerId;
        this.number = number;
        this.answer = answer;
    }
}
