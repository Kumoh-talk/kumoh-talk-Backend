package com.example.demo.global.jwt.exception;

import com.example.demo.global.base.exception.ErrorCode;

public class JwtNotExistException extends JwtAuthenticationException {
    public JwtNotExistException() {
        super(ErrorCode.JWT_NOT_EXIST);
    }
}
