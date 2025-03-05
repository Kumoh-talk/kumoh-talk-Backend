package com.example.demo.global.converter;

import com.example.demo.domain.recruitment_board.entity.vo.EntireBoardType;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import org.springframework.core.convert.converter.Converter;

public class EntireBoardTypeConverter implements Converter<String, EntireBoardType> {
    @Override
    public EntireBoardType convert(String source) {
        for (EntireBoardType entireBoardType : EntireBoardType.values()) {
            if (entireBoardType.toString().equalsIgnoreCase(source)) {
                return entireBoardType;
            }
        }
        throw new ServiceException(ErrorCode.ENTIRE_BOARD_TYPE_PARSE_ERROR);
    }
}
