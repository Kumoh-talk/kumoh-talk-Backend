package com.example.demo.domain.recruitment_application.domain.response;

import com.example.demo.domain.recruitment_application.domain.entity.RecruitmentApplicantAnswer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class RecruitmentApplicantAnswerInfoResponse {
    private Long answerId;
    private Long questionId;
    private Integer number;
    private String question;
    private String answer;

    public static RecruitmentApplicantAnswerInfoResponse from(RecruitmentApplicantAnswer applicantAnswerEntity) {
        return RecruitmentApplicantAnswerInfoResponse.builder()
                .answerId(applicantAnswerEntity.getId())
                .questionId(applicantAnswerEntity.getRecruitmentFormQuestion().getId())
                .number(applicantAnswerEntity.getRecruitmentFormQuestion().getNumber())
                .question(applicantAnswerEntity.getRecruitmentFormQuestion().getQuestion())
                .answer(applicantAnswerEntity.getAnswer())
                .build();
    }
}
