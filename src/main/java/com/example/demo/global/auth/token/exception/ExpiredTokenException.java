package com.example.demo.global.auth.token.exception;

import com.example.demo.global.base.exception.ErrorCode;

public class ExpiredTokenException extends TokenException{
    private static final ErrorCode ERROR_CODE = ErrorCode.EXPIRED_TOKEN;

    public ExpiredTokenException() {
        super(ERROR_CODE);
    }
}
