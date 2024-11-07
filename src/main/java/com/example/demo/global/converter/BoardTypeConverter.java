package com.example.demo.global.converter;

import com.example.demo.domain.recruitment_board.domain.vo.BoardType;
import org.springframework.core.convert.converter.Converter;

public class BoardTypeConverter implements Converter<String, BoardType> {
    @Override
    public BoardType convert(String source) {
        for (BoardType boardType : BoardType.values()) {
            if (boardType.toString().equalsIgnoreCase(source)) {
                return boardType;
            }
        }
        return null;
    }
}
