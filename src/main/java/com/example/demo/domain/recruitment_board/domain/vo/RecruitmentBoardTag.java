package com.example.demo.domain.recruitment_board.domain.vo;

import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum RecruitmentBoardTag {
    FRONTEND,
    BACKEND,
    AI,
    MOBILE,
    SECURITY;

    public static final String errorMsg = "태그는 frontend, backend, ai, mobile, security 중 하나여야 합니다.";

    @JsonCreator
    public static RecruitmentBoardTag fromString(String value) {
        if (value == null) {
            return null;
        }

        for (RecruitmentBoardTag recruitmentBoardTag : RecruitmentBoardTag.values()) {
            if (recruitmentBoardTag.toString().equalsIgnoreCase(value)) {
                return recruitmentBoardTag;
            }
        }
        throw new ServiceException(ErrorCode.RECRUITMENT_BOARD_TAG_PARSE_ERROR);
    }
}
