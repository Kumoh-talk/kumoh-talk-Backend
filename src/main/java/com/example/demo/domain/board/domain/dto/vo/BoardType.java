package com.example.demo.domain.board.domain.dto.vo;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum BoardType {
    SEMINAR,
    NOTICE
    ;

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
