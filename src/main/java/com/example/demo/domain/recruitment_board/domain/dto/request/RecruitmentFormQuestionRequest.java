package com.example.demo.domain.recruitment_board.domain.dto.request;

import com.example.demo.domain.recruitment_board.domain.vo.QuestionType;
import com.example.demo.global.aop.valid.ValidAnswerList;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@ValidAnswerList
@Schema(name = "RecruitmentFormQuestionRequest", description = "모집 게시물 신청폼 요청")
public class RecruitmentFormQuestionRequest {
    @Schema(description = "신청폼 질문 순서 정보", example = "1")
    @NotNull(message = "질문 번호를 작성해야합니다.")
    private Integer number;

    @Schema(description = "신청폼 질문 내용 정보", example = "what is your hobby?")
    @NotBlank(message = "질문을 작성해야합니다.")
    @Size(min = 1, max = 100, message = "질문 최대 길이는 100글자 입니다.")
    private String question;

    @Schema(description = "신청폼 질문 타입 정보[choice, description, checkbox]", example = "description")
    @NotNull(message = "질문 타입을 선택해야합니다.")
    private QuestionType type;

    @Schema(description = "신청폼 질문 필수여부 정보", example = "true")
    @NotNull(message = "질문 필수 여부를 선택해야합니다.")
    private Boolean isEssential;

    @Schema(description = "신청폼 질문 선택형 답안 리스트(description 질문일 경우 빈 리스트를 삽입([]))")
    @Valid
    private List<RecruitmentFormAnswerRequest> answerList = new ArrayList<>();
}
