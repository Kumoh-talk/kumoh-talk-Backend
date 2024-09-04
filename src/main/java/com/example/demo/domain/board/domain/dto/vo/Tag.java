package com.example.demo.domain.board.domain.dto.vo;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Tag {
    SEMINAR,
    NOTICE
    ;

    @JsonCreator
    public static Tag fromString(String value) {
        for(Tag tag : Tag.values()) {
            if(tag.toString().equalsIgnoreCase(value)) {
                return tag;
            }
        }
        return null;
    }
}
