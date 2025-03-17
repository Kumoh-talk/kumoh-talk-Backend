package com.example.demo.application.recruitment_board.dto.request;

import com.example.demo.domain.recruitment_board.entity.RecruitmentFormAnswerInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(name = "RecruitmentFormAnswerRequest", description = "신청폼 질문 선택형 답안 요청")
public class RecruitmentFormAnswerRequest {
    @Schema(description = "선택형 답안 순서 정보", example = "1")
    @NotNull(message = "답변 번호를 작성해야합니다.")
    private Integer number;

    @Schema(description = "선택형 답안 내용 정보", example = "answer1")
    @NotBlank(message = "지정 답안을 작성해야합니다.")
    @Size(min = 1, max = 50, message = "객관식 선택지 최대 길이는 50글자 입니다.")
    private String answer;

    public RecruitmentFormAnswerInfo toDomain() {
        return RecruitmentFormAnswerInfo.builder()
                .number(number)
                .answer(answer)
                .build();
    }
}
