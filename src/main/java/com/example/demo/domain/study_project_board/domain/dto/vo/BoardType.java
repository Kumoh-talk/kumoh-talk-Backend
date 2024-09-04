package com.example.demo.domain.study_project_board.domain.dto.vo;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum BoardType {
    STUDY,
    PROJECT,
    SEMINAR_NOTICE,
    SEMINAR_SUMMARY;

    @JsonCreator
    public static BoardType fromString(String value) {
        for (BoardType boardType : BoardType.values()) {
            if (boardType.toString().equalsIgnoreCase(value)) {
                return boardType;
            }
        }
        return null;
    }
}
