package com.example.demo.domain.board.domain.dto.vo;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Status {
    DRAFT,
    PUBLISHED
    ;

    @JsonCreator
    public static Status fromString(String value) {
        for(Status status : Status.values()) {
            if(status.toString().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Status에 해당하는 값이 없습니다.");
    }
}
