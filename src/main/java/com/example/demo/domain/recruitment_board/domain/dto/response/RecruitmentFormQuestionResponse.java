package com.example.demo.domain.recruitment_board.domain.dto.response;

import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentFormChoiceAnswer;
import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentFormQuestion;
import com.example.demo.domain.recruitment_board.domain.vo.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruitmentFormQuestionResponse {
    private Long questionId;

    private Integer number;

    private String question;

    private QuestionType type;

    private Boolean isEssential;

    private List<RecruitmentFormChoiceAnswerResponse> answerList = new ArrayList<>();

    public static RecruitmentFormQuestionResponse from(
            RecruitmentFormQuestion recruitmentFormQuestionEntity) {
        return RecruitmentFormQuestionResponse.builder()
                .questionId(recruitmentFormQuestionEntity.getId())
                .number(recruitmentFormQuestionEntity.getNumber())
                .question(recruitmentFormQuestionEntity.getQuestion())
                .type(recruitmentFormQuestionEntity.getType())
                .isEssential(recruitmentFormQuestionEntity.getIsEssential())
                .answerList(recruitmentFormQuestionEntity.getRecruitmentFormChoiceAnswerList().stream()
                        .map(RecruitmentFormChoiceAnswerResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class RecruitmentFormChoiceAnswerResponse {
        private Long answerId;

        private Integer number;

        private String answer;

        public static RecruitmentFormChoiceAnswerResponse from(
                RecruitmentFormChoiceAnswer recruitmentFormChoiceAnswerEntity) {
            return RecruitmentFormChoiceAnswerResponse.builder()
                    .answerId(recruitmentFormChoiceAnswerEntity.getId())
                    .number(recruitmentFormChoiceAnswerEntity.getNumber())
                    .answer(recruitmentFormChoiceAnswerEntity.getAnswer())
                    .build();
        }
    }
}
