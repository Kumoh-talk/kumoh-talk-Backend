package com.example.demo.domain.recruitment_application.domain.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class RecruitmentApplicationRequest {

    @NotNull
    @Valid
    private List<RecruitmentApplicantApplicationRequest> application;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RecruitmentApplicantApplicationRequest {
        @NotNull(message = "대상 질문 id를 입력해야합니다.")
        private Long questionId;

        @NotNull(message = "답변을 입력해야합니다.")
        @Valid
        private List<RecruitmentApplicantAnswerInfoRequest> answerList;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RecruitmentApplicantAnswerInfoRequest {
        private Integer number;

        @Size(min = 1, max = 1000, message = "답변 최대 길이는 1000글자 입니다.")
        private String answer;
    }
}
