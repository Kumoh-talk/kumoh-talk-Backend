package com.example.demo.global.converter;

import com.example.demo.domain.comment.domain.vo.CommentTargetBoardType;
import org.springframework.core.convert.converter.Converter;

public class CommentTargetBoardTypeConverter implements Converter<String, CommentTargetBoardType> {
    @Override
    public CommentTargetBoardType convert(String source) {
        for (CommentTargetBoardType boardType : CommentTargetBoardType.values()) {
            if (boardType.toString().equalsIgnoreCase(source)) {
                return boardType;
            }
        }
        return null;
    }
}