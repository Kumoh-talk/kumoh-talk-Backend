package com.example.demo.domain.study_project_board.domain.dto.response;

import com.example.demo.domain.study_project_board.domain.entity.StudyProjectBoard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudyProjectBoardInfoAndFormResponse {

    private StudyProjectBoardInfoResponse board;

    private List<StudyProjectFormQuestionResponse> form = new ArrayList<>();

    public static StudyProjectBoardInfoAndFormResponse from(StudyProjectBoard studyProjectBoard) {
        return StudyProjectBoardInfoAndFormResponse
                .builder()
                .board(StudyProjectBoardInfoResponse.from(studyProjectBoard))
                .form(studyProjectBoard.getStudyProjectFormQuestionList().stream()
                        .map(StudyProjectFormQuestionResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }

}
