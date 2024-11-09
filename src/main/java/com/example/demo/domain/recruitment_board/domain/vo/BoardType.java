package com.example.demo.domain.recruitment_board.domain.vo;

import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum BoardType {
    STUDY,
    PROJECT,
    MENTORING,
    SEMINAR_NOTICE,
    SEMINAR_SUMMARY;

    public static final String errorMsg = "게시물 타입은 study, project, mentoring, seminar_notice, seminar_summary 중 하나여야 합니다.";

    @JsonCreator
    public static BoardType fromString(String value) {
        if (value == null) {
            return null;
        }

        for (BoardType boardType : BoardType.values()) {
            if (boardType.toString().equalsIgnoreCase(value)) {
                return boardType;
            }
        }
        throw new ServiceException(ErrorCode.BOARD_TYPE_PARSE_ERROR);

    }
}
