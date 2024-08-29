package com.example.demo.domain.study_project_board.domain.dto.request;

import com.example.demo.domain.study_project_board.domain.dto.vo.QuestionType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class StudyProjectFormQuestionRequest {
    @NotNull(message = "질문 번호를 작성해야합니다.")
    private Integer number;

    @NotBlank(message = "질문을 작성해야합니다.")
    private String question;

    @NotNull(message = "질문 타입을 선택해야합니다.")
    private QuestionType type;

    @NotNull(message = "질문 필수 여부를 선택해야합니다.")
    private Boolean isEssential;

    // TODO : 커스텀 validation 적용 예정(객관식 타입이면 널 불가능 / 주관식 타입이면 널)
    @Valid
    private List<StudyProjectFormChoiceAnswerRequest> answerList = new ArrayList<>();
}
