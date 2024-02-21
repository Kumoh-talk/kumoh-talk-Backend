package com.example.demo.global.auth.token.exception;

import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;

public abstract class TokenException extends ServiceException {

    protected TokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}