package com.example.demo.domain.board.domain.dto.vo;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Status {
    DRAFT,
    PUBLISHED;
    public static final String errorMsg = "상태는 draft, published 중 하나여야 합니다.";

    @JsonCreator
    public static Status fromString(String value) {
        for (Status status : Status.values()) {
            if (status.toString().equalsIgnoreCase(value)) {
                return status;
            }
        }
        return null;
    }
}
