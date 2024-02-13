package com.example.demo.global.base.exception;

import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException{
    private final ErrorCode errorCode;

    private final String messageKey;

    private final Object[] params;

    protected ServiceException(ErrorCode errorCode, String messageKey, Object[] params) {
        this.errorCode = errorCode;
        this.messageKey = messageKey;
        this.params = params;
    }
}
