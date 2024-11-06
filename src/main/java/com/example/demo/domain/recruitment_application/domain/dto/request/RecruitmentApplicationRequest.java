package com.example.demo.domain.recruitment_application.domain.dto.request;

import com.example.demo.domain.recruitment_application.domain.entity.RecruitmentApplicantAnswer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class RecruitmentApplicationRequest {
    @Valid
    private List<RecruitmentApplicantAnswerInfoRequest> answerList;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RecruitmentApplicantAnswerInfoRequest {
        @NotNull(message = "대상 질문 id를 입력해야합니다.")
        private Long questionId;
        @NotBlank(message = "질문 답변이 공백이면 안됩니다.")
        private String answer;

        public static RecruitmentApplicantAnswerInfoRequest from(RecruitmentApplicantAnswer applicantAnswerEntity) {
            return RecruitmentApplicantAnswerInfoRequest.builder()
                    .questionId(applicantAnswerEntity.getId())
                    .answer(applicantAnswerEntity.getAnswer())
                    .build();
        }
    }
}
