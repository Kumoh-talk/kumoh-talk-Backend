package com.example.demo.global.auth.token.exception;

import com.example.demo.global.base.exception.ErrorCode;

public class InvalidTokenException extends TokenException{
    private static final ErrorCode ERROR_CODE = ErrorCode.INVALID_TOKEN;

    public InvalidTokenException() {
        super(ERROR_CODE);
    }
}
