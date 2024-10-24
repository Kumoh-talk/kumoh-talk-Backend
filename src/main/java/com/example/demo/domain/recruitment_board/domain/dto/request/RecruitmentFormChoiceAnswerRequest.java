package com.example.demo.domain.recruitment_board.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RecruitmentFormChoiceAnswerRequest {
    @NotNull(message = "답변 번호를 작성해야합니다.")
    private Integer number;

    @NotBlank(message = "지정 답안을 작성해야합니다.")
    private String answer;
}
