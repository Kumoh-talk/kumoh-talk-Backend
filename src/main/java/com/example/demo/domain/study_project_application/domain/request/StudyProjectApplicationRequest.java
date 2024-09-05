package com.example.demo.domain.study_project_application.domain.request;

import com.example.demo.domain.study_project_application.domain.entity.StudyProjectApplicantAnswer;
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
public class StudyProjectApplicationRequest {
    @Valid
    private List<StudyProjectApplicantAnswerInfoRequest> answerList;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StudyProjectApplicantAnswerInfoRequest {
        @NotNull(message = "대상 질문 id를 입력해야합니다.")
        private Long questionId;
        @NotBlank(message = "질문 답변이 공백이면 안됩니다.")
        private String answer;

        public static StudyProjectApplicantAnswerInfoRequest from(StudyProjectApplicantAnswer applicantAnswerEntity) {
            return StudyProjectApplicantAnswerInfoRequest.builder()
                    .questionId(applicantAnswerEntity.getId())
                    .answer(applicantAnswerEntity.getAnswer())
                    .build();
        }
    }
}
