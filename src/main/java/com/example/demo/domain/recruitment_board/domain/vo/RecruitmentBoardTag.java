package com.example.demo.domain.recruitment_board.domain.vo;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum RecruitmentBoardTag {
    FRONTEND,
    BACKEND,
    AI,
    MOBILE,
    SECURITY;

    @JsonCreator
    public static RecruitmentBoardTag fromString(String value) {
        for (RecruitmentBoardTag recruitmentBoardTag : RecruitmentBoardTag.values()) {
            if (recruitmentBoardTag.toString().equalsIgnoreCase(value)) {
                return recruitmentBoardTag;
            }
        }
        return null;
    }
}
