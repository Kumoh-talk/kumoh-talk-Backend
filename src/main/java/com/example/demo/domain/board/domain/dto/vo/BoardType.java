package com.example.demo.domain.board.domain.dto.vo;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum BoardType {
    SEMINAR,
    NOTICE
    ;

    public static final String errorMsg = "게시판 타입은 seminar, notice 중 하나여야 합니다.";
    @JsonCreator
    public static BoardType fromString(String value) {
        for(BoardType boardType : BoardType.values()) {
            if(boardType.toString().equals(value)) {
                return boardType;
            }
        }
        return null;
    }
}
