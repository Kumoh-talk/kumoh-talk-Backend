package com.example.demo.domain.study_project_board.domain.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class StudyProjectBoardInfoAndFormRequest {
    @NotNull(message = "게시물을 등록해야합니다.")
    @Valid
    private StudyProjectBoardInfoRequest board;

    @NotNull(message = "질문이 널이면 안됩니다.")
    @Size(min = 1, message = "질문을 1개 이상 등록해야합니다.")
    @Valid
    private List<StudyProjectFormQuestionRequest> form = new ArrayList<>();
}
