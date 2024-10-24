package com.example.demo.domain.recruitment_board.domain.dto.vo;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum RecruitmentBoardType {
    STUDY,
    PROJECT,
    MENTORING;

    @JsonCreator
    public static RecruitmentBoardType fromString(String value) {
        for (RecruitmentBoardType recruitmentBoardType : RecruitmentBoardType.values()) {
            if (recruitmentBoardType.toString().equalsIgnoreCase(value)) {
                return recruitmentBoardType;
            }
        }
        return null;
    }
}

