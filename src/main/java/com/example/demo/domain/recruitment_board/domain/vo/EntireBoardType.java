package com.example.demo.domain.recruitment_board.domain.vo;

import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum EntireBoardType {
    STUDY,
    PROJECT,
    MENTORING,
    SEMINAR_NOTICE,
    SEMINAR_SUMMARY;

    public static final String errorMsg = "게시물 타입은 study, project, mentoring, seminar_notice, seminar_summary 중 하나여야 합니다.";

    @JsonCreator
    public static EntireBoardType fromString(String value) {
        if (value == null) {
            return null;
        }

        for (EntireBoardType entireBoardType : EntireBoardType.values()) {
            if (entireBoardType.toString().equalsIgnoreCase(value)) {
                return entireBoardType;
            }
        }
        throw new ServiceException(ErrorCode.ENTIRE_BOARD_TYPE_PARSE_ERROR);

    }
}
