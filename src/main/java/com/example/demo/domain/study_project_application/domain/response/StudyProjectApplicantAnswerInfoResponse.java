package com.example.demo.domain.study_project_application.domain.response;

import com.example.demo.domain.study_project_application.domain.entity.StudyProjectApplicantAnswer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class StudyProjectApplicantAnswerInfoResponse {
    private Long answerId;
    private Long questionId;
    private Integer number;
    private String question;
    private String answer;

    public static StudyProjectApplicantAnswerInfoResponse from(StudyProjectApplicantAnswer applicantAnswerEntity) {
        return StudyProjectApplicantAnswerInfoResponse.builder()
                .answerId(applicantAnswerEntity.getId())
                .questionId(applicantAnswerEntity.getStudyProjectFormQuestion().getId())
                .number(applicantAnswerEntity.getStudyProjectFormQuestion().getNumber())
                .question(applicantAnswerEntity.getStudyProjectFormQuestion().getQuestion())
                .answer(applicantAnswerEntity.getAnswer())
                .build();
    }
}
