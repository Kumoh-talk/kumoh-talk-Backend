package com.example.demo.domain.recruitment_board.domain.dto.request;

import com.example.demo.domain.recruitment_board.domain.vo.QuestionType;
import com.example.demo.global.aop.valid.ValidAnswerList;
import com.example.demo.global.aop.valid.ValidEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@ValidAnswerList
public class RecruitmentFormQuestionRequest {
    @NotNull(message = "질문 번호를 작성해야합니다.")
    private Integer number;

    @NotBlank(message = "질문을 작성해야합니다.")
    private String question;

    @ValidEnum(enumClass = QuestionType.class, message = "질문 타입은 CHOICE, DESCRIPTION 중 하나여야 합니다.")
    private QuestionType type;

    @NotNull(message = "질문 필수 여부를 선택해야합니다.")
    private Boolean isEssential;

    @Valid
    private List<RecruitmentFormChoiceAnswerRequest> answerList = new ArrayList<>();
}
