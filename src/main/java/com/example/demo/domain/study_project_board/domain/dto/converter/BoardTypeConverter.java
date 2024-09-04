package com.example.demo.domain.study_project_board.domain.dto.converter;

import com.example.demo.domain.study_project_board.domain.dto.vo.BoardType;
import org.springframework.core.convert.converter.Converter;

public class BoardTypeConverter implements Converter<String, BoardType> {
    @Override
    public BoardType convert(String value) {
        return BoardType.fromString(value);
    }
}
