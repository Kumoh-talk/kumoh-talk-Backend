package com.example.demo.global.converter;

import com.example.demo.domain.board.domain.dto.vo.Status;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import org.springframework.core.convert.converter.Converter;

public class StatusConverter implements Converter<String, Status> {
    @Override
    public Status convert(String source) {
        for (Status status : Status.values()) {
            if (status.toString().equalsIgnoreCase(source)) {
                return status;
            }
        }
        throw new ServiceException(ErrorCode.STATUS_PARSE_ERROR);
    }
}
