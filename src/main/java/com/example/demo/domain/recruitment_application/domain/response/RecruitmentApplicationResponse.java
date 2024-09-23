package com.example.demo.domain.recruitment_application.domain.response;

import com.example.demo.domain.recruitment_application.domain.entity.RecruitmentApplicant;
import com.example.demo.domain.recruitment_application.domain.entity.RecruitmentApplicantAnswer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
public class RecruitmentApplicationResponse {
    private final Long applicantId;
    private final List<RecruitmentApplicantAnswerInfoResponse> applicantAnswer;

    public static RecruitmentApplicationResponse from(RecruitmentApplicant applicant,
                                                      List<RecruitmentApplicantAnswer> saveApplicantAnswerList) {
        return RecruitmentApplicationResponse.builder()
                .applicantId(applicant.getId())
                .applicantAnswer(saveApplicantAnswerList.stream()
                        .map(RecruitmentApplicantAnswerInfoResponse::from)
                        .sorted(Comparator.comparing(RecruitmentApplicantAnswerInfoResponse::getNumber))
                        .collect(Collectors.toList()))
                .build();
    }
}
