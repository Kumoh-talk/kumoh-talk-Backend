package com.example.demo.global.converter;

import com.example.demo.domain.study_project_board.domain.dto.vo.StudyProjectBoardType;
import org.springframework.core.convert.converter.Converter;

public class StudyProjectBoardTypeConverter implements Converter<String, StudyProjectBoardType> {
    @Override
    public StudyProjectBoardType convert(String value) {
        return StudyProjectBoardType.fromString(value);
    }
}
