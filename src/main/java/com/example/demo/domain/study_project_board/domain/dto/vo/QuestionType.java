package com.example.demo.domain.study_project_board.domain.dto.vo;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum QuestionType {
    CHOICE,
    DESCRIPTION;

    @JsonCreator
    public static QuestionType fromString(String value) {
        for (QuestionType questionType : QuestionType.values()) {
            if (questionType.toString().equalsIgnoreCase(value)) {
                return questionType;
            }
        }
        return null;
    }
}
