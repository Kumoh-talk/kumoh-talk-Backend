package com.example.demo.domain.study_project_board.domain.dto.converter;

import com.example.demo.domain.board.domain.dto.vo.Status;
import org.springframework.core.convert.converter.Converter;

public class StatusConverter implements Converter<String, Status> {
    @Override
    public Status convert(String value) {
        return Status.fromString(value);
    }
}
