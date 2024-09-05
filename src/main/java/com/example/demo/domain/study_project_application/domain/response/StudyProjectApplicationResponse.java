package com.example.demo.domain.study_project_application.domain.response;

import com.example.demo.domain.study_project_application.domain.entity.StudyProjectApplicant;
import com.example.demo.domain.study_project_application.domain.entity.StudyProjectApplicantAnswer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
public class StudyProjectApplicationResponse {
    private final Long applicantId;
    private final List<StudyProjectApplicantAnswerInfoResponse> applicantAnswer;

    public static StudyProjectApplicationResponse from(StudyProjectApplicant applicant,
                                                       List<StudyProjectApplicantAnswer> saveApplicantAnswerList) {
        return StudyProjectApplicationResponse.builder()
                .applicantId(applicant.getId())
                .applicantAnswer(saveApplicantAnswerList.stream()
                        .map(StudyProjectApplicantAnswerInfoResponse::from)
                        .sorted(Comparator.comparing(StudyProjectApplicantAnswerInfoResponse::getNumber))
                        .collect(Collectors.toList()))
                .build();
    }
}
