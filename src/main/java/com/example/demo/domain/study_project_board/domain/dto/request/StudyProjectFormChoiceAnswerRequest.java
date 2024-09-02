package com.example.demo.domain.study_project_board.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StudyProjectFormChoiceAnswerRequest {
    @NotNull(message = "답변 번호를 작성해야합니다.")
    private Integer number;

    @NotBlank(message = "지정 답안을 작성해야합니다.")
    private String answer;
}
