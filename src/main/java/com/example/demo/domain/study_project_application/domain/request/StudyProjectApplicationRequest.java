package com.example.demo.domain.study_project_application.domain.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StudyProjectApplicationRequest {
    @NotNull(message = "대상 질문 id를 입력해야합니다.")
    private Long questionId;
    private String answer;
}
