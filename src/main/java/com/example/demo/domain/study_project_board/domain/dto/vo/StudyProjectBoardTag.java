package com.example.demo.domain.study_project_board.domain.dto.vo;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum StudyProjectBoardTag {
    FRONTEND,
    BACKEND,
    AI,
    MOBILE,
    SECURITY;

    @JsonCreator
    public static StudyProjectBoardTag fromString(String value) {
        for (StudyProjectBoardTag studyProjectBoardTag : StudyProjectBoardTag.values()) {
            if (studyProjectBoardTag.toString().equalsIgnoreCase(value)) {
                return studyProjectBoardTag;
            }
        }
        return null;
    }
}
