package com.example.demo.domain.study_project_board.domain.dto.vo;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum StudyProjectBoardType {
    STUDY,
    PROJECT;

    @JsonCreator
    public static StudyProjectBoardType fromString(String value) {
        for (StudyProjectBoardType studyProjectBoardType : StudyProjectBoardType.values()) {
            if (studyProjectBoardType.toString().equalsIgnoreCase(value)) {
                return studyProjectBoardType;
            }
        }
        return null;
    }
}

