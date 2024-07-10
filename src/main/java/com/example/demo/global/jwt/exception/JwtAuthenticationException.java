package com.example.demo.global.jwt.exception;

import org.springframework.security.core.AuthenticationException;

import com.example.demo.global.base.exception.ErrorCode;

import lombok.Getter;

@Getter
public class JwtAuthenticationException extends AuthenticationException {
    private ErrorCode errorCode;

    public JwtAuthenticationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public JwtAuthenticationException(Throwable cause, ErrorCode errorCode) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }
}
