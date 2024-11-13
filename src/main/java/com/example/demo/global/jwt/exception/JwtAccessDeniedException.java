package com.example.demo.global.jwt.exception;

import com.example.demo.global.base.exception.ErrorCode;

public class JwtAccessDeniedException extends JwtAuthenticationException {
    public JwtAccessDeniedException() {
        super(ErrorCode.ACCESS_DENIED);
    }
}
