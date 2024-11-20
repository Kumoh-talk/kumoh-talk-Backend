package com.example.demo.domain.recruitment_board.domain.vo;

import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum RecruitmentBoardType {
    STUDY,
    PROJECT,
    MENTORING;

    public static final String errorMsg = "게시물 타입은 study, project, mentoring 중 하나여야 합니다.";

    @JsonCreator
    public static RecruitmentBoardType fromString(String value) {
        if (value == null) {
            return null;
        }

        for (RecruitmentBoardType recruitmentBoardType : RecruitmentBoardType.values()) {
            if (recruitmentBoardType.toString().equalsIgnoreCase(value)) {
                return recruitmentBoardType;
            }
        }
        throw new ServiceException(ErrorCode.RECRUITMENT_BOARD_TYPE_PARSE_ERROR);
    }
}

