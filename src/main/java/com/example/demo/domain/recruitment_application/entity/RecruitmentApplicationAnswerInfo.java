package com.example.demo.domain.recruitment_application.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class RecruitmentApplicationAnswerInfo {
    private Long questionId;
    private Integer questionNumber;
    private String question;
    private List<AnswerInfo> answerInfoList;

    @Getter
    @AllArgsConstructor
    @Builder
    public static class AnswerInfo {
        private Long answerId;
        private Integer answerNumber;
        private String answer;
    }
}
