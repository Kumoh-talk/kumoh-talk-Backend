package com.example.demo.global.converter;

import com.example.demo.domain.recruitment_board.domain.vo.BoardType;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import org.springframework.core.convert.converter.Converter;

public class BoardTypeConverter implements Converter<String, BoardType> {
    @Override
    public BoardType convert(String source) {
        for (BoardType boardType : BoardType.values()) {
            if (boardType.toString().equalsIgnoreCase(source)) {
                return boardType;
            }
        }
        throw new ServiceException(ErrorCode.BOARD_TYPE_PARSE_ERROR);
    }
}
