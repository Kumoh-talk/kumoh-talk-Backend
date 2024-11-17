package com.example.demo.domain.recruitment_application.domain.dto.response;

import com.example.demo.domain.recruitment_application.domain.entity.RecruitmentApplicantDescriptiveAnswer;
import com.example.demo.domain.recruitment_application.domain.entity.RecruitmentApplicantOptionalAnswer;
import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentFormQuestion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class RecruitmentApplicantAnswerInfoResponse {
    private Long questionId;
    private Integer questionNumber;
    private String question;
    private List<Answer> answerList;

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Answer {
        private Long answerId;
        private Integer answerNumber;
        private String answer;

        public static Answer from(RecruitmentApplicantDescriptiveAnswer applicantAnswerEntity) {
            return Answer.builder()
                    .answerId(applicantAnswerEntity.getId())
                    .answerNumber(null)
                    .answer(applicantAnswerEntity.getAnswer())
                    .build();
        }

        public static Answer from(RecruitmentApplicantOptionalAnswer applicantAnswerEntity) {
            return Answer.builder()
                    .answerId(applicantAnswerEntity.getId())
                    .answerNumber(applicantAnswerEntity.getRecruitmentFormAnswer().getNumber())
                    .answer(applicantAnswerEntity.getRecruitmentFormAnswer().getAnswer())
                    .build();
        }
    }

    public static RecruitmentApplicantAnswerInfoResponse from(RecruitmentApplicantDescriptiveAnswer applicantAnswerEntity) {
        RecruitmentApplicantAnswerInfoResponse response = RecruitmentApplicantAnswerInfoResponse.builder()
                .questionId(applicantAnswerEntity.getRecruitmentFormQuestion().getId())
                .questionNumber(applicantAnswerEntity.getRecruitmentFormQuestion().getNumber())
                .question(applicantAnswerEntity.getRecruitmentFormQuestion().getQuestion())
                .answerList(new ArrayList<>())
                .build();
        response.getAnswerList().add(Answer.from(applicantAnswerEntity));
        return response;
    }

    public static RecruitmentApplicantAnswerInfoResponse from(RecruitmentApplicantOptionalAnswer applicantAnswerEntity) {
        RecruitmentApplicantAnswerInfoResponse response = RecruitmentApplicantAnswerInfoResponse.builder()
                .questionId(applicantAnswerEntity.getRecruitmentFormAnswer().getRecruitmentFormQuestion().getId())
                .questionNumber(applicantAnswerEntity.getRecruitmentFormAnswer().getRecruitmentFormQuestion().getNumber())
                .question(applicantAnswerEntity.getRecruitmentFormAnswer().getRecruitmentFormQuestion().getQuestion())
                .answerList(new ArrayList<>())
                .build();
        response.getAnswerList().add(Answer.from(applicantAnswerEntity));
        return response;
    }

    public static RecruitmentApplicantAnswerInfoResponse from(RecruitmentFormQuestion recruitmentFormQuestion) {
        RecruitmentApplicantAnswerInfoResponse response = RecruitmentApplicantAnswerInfoResponse.builder()
                .questionId(recruitmentFormQuestion.getId())
                .questionNumber(recruitmentFormQuestion.getNumber())
                .question(recruitmentFormQuestion.getQuestion())
                .answerList(new ArrayList<>())
                .build();
        return response;
    }
}
