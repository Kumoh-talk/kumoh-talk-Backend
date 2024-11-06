package com.example.demo.global.converter;

import com.example.demo.domain.recruitment_board.domain.vo.BoardType;
import org.springframework.core.convert.converter.Converter;

public class BoardTypeConverter implements Converter<String, BoardType> {
    @Override
    public BoardType convert(String value) {
        return BoardType.fromString(value);
    }
}
