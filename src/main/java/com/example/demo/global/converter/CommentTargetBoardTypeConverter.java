package com.example.demo.global.converter;

import com.example.demo.domain.comment.domain.vo.CommentTargetBoardType;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import org.springframework.core.convert.converter.Converter;

public class CommentTargetBoardTypeConverter implements Converter<String, CommentTargetBoardType> {
    @Override
    public CommentTargetBoardType convert(String source) {
        for (CommentTargetBoardType boardType : CommentTargetBoardType.values()) {
            if (boardType.toString().equalsIgnoreCase(source)) {
                return boardType;
            }
        }
        throw new ServiceException(ErrorCode.COMMENT_TARGET_BOARD_TYPE_PARSE_ERROR);
    }
}