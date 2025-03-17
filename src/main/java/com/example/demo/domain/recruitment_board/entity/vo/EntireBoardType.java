package com.example.demo.domain.recruitment_board.entity.vo;

import com.example.demo.domain.board.service.entity.vo.BoardType;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum EntireBoardType {
    STUDY(RecruitmentBoardType.STUDY),
    PROJECT(RecruitmentBoardType.PROJECT),
    MENTORING(RecruitmentBoardType.MENTORING),
    SEMINAR_NOTICE(BoardType.NOTICE),
    SEMINAR_SUMMARY(BoardType.SEMINAR);

    EntireBoardType(RecruitmentBoardType recruitmentBoardType) {
        this.boardType = null;
        this.recruitmentBoardType = recruitmentBoardType;
    }

    EntireBoardType(BoardType boardType) {
        this.boardType = boardType;
        this.recruitmentBoardType = null;
    }

    private final BoardType boardType;
    private final RecruitmentBoardType recruitmentBoardType;

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

    public static EntireBoardType fromBoardType(BoardType boardType) {
        for (EntireBoardType entireBoardType : EntireBoardType.values()) {
            if (entireBoardType.boardType == boardType) {
                return entireBoardType;
            }
        }
        throw new ServiceException(ErrorCode.ENTIRE_BOARD_TYPE_PARSE_ERROR);
    }

    public static EntireBoardType fromRecruitmentBoardType(RecruitmentBoardType recruitmentBoardType) {
        for (EntireBoardType entireBoardType : EntireBoardType.values()) {
            if (entireBoardType.recruitmentBoardType == recruitmentBoardType) {
                return entireBoardType;
            }
        }
        throw new ServiceException(ErrorCode.ENTIRE_BOARD_TYPE_PARSE_ERROR);
    }
}
