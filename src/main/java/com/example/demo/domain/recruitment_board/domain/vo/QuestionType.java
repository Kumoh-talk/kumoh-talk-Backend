package com.example.demo.domain.recruitment_board.domain.vo;

import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum QuestionType {
    CHOICE,
    DESCRIPTION,
    CHECKBOX;

    public static final String errorMsg = "질문 타입은 choice, description, checkbox 중 하나여야 합니다.";

    @JsonCreator
    public static QuestionType fromString(String value) {
        if (value == null) {
            return null;
        }

        for (QuestionType questionType : QuestionType.values()) {
            if (questionType.toString().equalsIgnoreCase(value)) {
                return questionType;
            }
        }
        throw new ServiceException(ErrorCode.QUESTION_TYPE_PARSE_ERROR);
    }
}
