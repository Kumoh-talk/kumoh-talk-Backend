package com.example.demo.domain.recruitment_board.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RecruitmentFormAnswerRequest {
    @NotNull(message = "답변 번호를 작성해야합니다.")
    private Integer number;

    @NotBlank(message = "지정 답안을 작성해야합니다.")
    @Size(min = 1, max = 50, message = "객관식 선택지 최대 길이는 50글자 입니다.")
    private String answer;
}
