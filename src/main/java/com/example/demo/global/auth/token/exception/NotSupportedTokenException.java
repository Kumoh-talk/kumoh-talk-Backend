package com.example.demo.global.auth.token.exception;

import com.example.demo.global.base.exception.ErrorCode;

public class NotSupportedTokenException extends TokenException{
    private static final ErrorCode ERROR_CODE = ErrorCode.NOT_SUPPORTED_TOKEN;

    public NotSupportedTokenException() {
        super(ERROR_CODE);
    }
}
