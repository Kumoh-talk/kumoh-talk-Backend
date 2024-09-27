package com.example.demo.global.converter;

import com.example.demo.domain.recruitment_board.domain.dto.vo.RecruitmentBoardType;
import org.springframework.core.convert.converter.Converter;

public class RecruitmentBoardTypeConverter implements Converter<String, RecruitmentBoardType> {
    @Override
    public RecruitmentBoardType convert(String value) {
        return RecruitmentBoardType.fromString(value);
    }
}
