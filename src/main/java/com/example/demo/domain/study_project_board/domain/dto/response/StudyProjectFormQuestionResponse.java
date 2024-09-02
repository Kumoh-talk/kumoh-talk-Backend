package com.example.demo.domain.study_project_board.domain.dto.response;

import com.example.demo.domain.study_project_board.domain.dto.vo.QuestionType;
import com.example.demo.domain.study_project_board.domain.entity.StudyProjectFormChoiceAnswer;
import com.example.demo.domain.study_project_board.domain.entity.StudyProjectFormQuestion;
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
public class StudyProjectFormQuestionResponse {
    private Long questionId;

    private Integer number;

    private String question;

    private QuestionType type;

    private Boolean isEssential;

    private List<StudyProjectFormChoiceAnswerResponse> answerList = new ArrayList<>();

    public static StudyProjectFormQuestionResponse from(
            StudyProjectFormQuestion studyProjectFormQuestionEntity) {
        return StudyProjectFormQuestionResponse.builder()
                .questionId(studyProjectFormQuestionEntity.getId())
                .number(studyProjectFormQuestionEntity.getNumber())
                .question(studyProjectFormQuestionEntity.getQuestion())
                .type(studyProjectFormQuestionEntity.getType())
                .isEssential(studyProjectFormQuestionEntity.getIsEssential())
                .answerList(studyProjectFormQuestionEntity.getStudyProjectFormChoiceAnswerList().stream()
                        .map(StudyProjectFormChoiceAnswerResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class StudyProjectFormChoiceAnswerResponse {
        private Long answerId;

        private Integer number;

        private String answer;

        public static StudyProjectFormChoiceAnswerResponse from(
                StudyProjectFormChoiceAnswer studyProjectFormChoiceAnswerEntity) {
            return StudyProjectFormChoiceAnswerResponse.builder()
                    .answerId(studyProjectFormChoiceAnswerEntity.getId())
                    .number(studyProjectFormChoiceAnswerEntity.getNumber())
                    .answer(studyProjectFormChoiceAnswerEntity.getAnswer())
                    .build();
        }
    }
}
