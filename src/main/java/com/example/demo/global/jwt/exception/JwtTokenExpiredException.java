package com.example.demo.global.jwt.exception;



import com.example.demo.global.base.exception.ErrorCode;

import lombok.Getter;

@Getter
public class JwtTokenExpiredException extends JwtAuthenticationException {

    public JwtTokenExpiredException(Throwable cause) {
        super(cause, ErrorCode.JWT_EXPIRED);
    }

}
