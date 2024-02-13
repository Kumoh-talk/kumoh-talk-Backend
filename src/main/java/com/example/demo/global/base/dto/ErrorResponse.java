package com.example.demo.global.base.dto;

import com.example.demo.global.base.exception.ErrorCode;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ErrorResponse{
    private String code;
    private String message;
    public static ErrorResponse of(ErrorCode code) {
        return new ErrorResponse(code.getCode(), code.getMessage());
    }
}
