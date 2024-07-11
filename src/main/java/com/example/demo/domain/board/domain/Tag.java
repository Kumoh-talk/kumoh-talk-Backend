package com.example.demo.domain.board.domain;

import com.example.demo.global.base.exception.ServiceException;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum Tag {
    seminar,
    notice
    ;

    @JsonCreator
    public static Tag fromString(String value) {
        for(Tag tag : Tag.values()) {
            if(tag.toString().equalsIgnoreCase(value)) {
                return tag;
            }
        }
        throw new IllegalArgumentException("tag에 해당하는 값이 없습니다.");
    }
}
