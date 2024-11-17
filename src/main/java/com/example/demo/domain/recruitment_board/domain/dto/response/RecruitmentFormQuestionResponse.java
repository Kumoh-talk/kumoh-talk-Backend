package com.example.demo.domain.recruitment_board.domain.dto.response;

import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentFormAnswer;
import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentFormQuestion;
import com.example.demo.domain.recruitment_board.domain.vo.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private List<RecruitmentFormChoiceAnswerResponse> answerList;

    public static RecruitmentFormQuestionResponse from(
            RecruitmentFormQuestion recruitmentFormQuestionEntity) {
        return RecruitmentFormQuestionResponse.builder()
                .questionId(recruitmentFormQuestionEntity.getId())
                .number(recruitmentFormQuestionEntity.getNumber())
                .question(recruitmentFormQuestionEntity.getQuestion())
                .type(recruitmentFormQuestionEntity.getType())
                .isEssential(recruitmentFormQuestionEntity.getIsEssential())
                .answerList(recruitmentFormQuestionEntity.getRecruitmentFormAnswerList().stream()
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
                RecruitmentFormAnswer recruitmentFormAnswerEntity) {
            return RecruitmentFormChoiceAnswerResponse.builder()
                    .answerId(recruitmentFormAnswerEntity.getId())
                    .number(recruitmentFormAnswerEntity.getNumber())
                    .answer(recruitmentFormAnswerEntity.getAnswer())
                    .build();
        }
    }
}
