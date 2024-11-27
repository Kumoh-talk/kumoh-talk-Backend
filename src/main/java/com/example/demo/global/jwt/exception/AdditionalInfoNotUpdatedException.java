package com.example.demo.global.jwt.exception;

import com.example.demo.global.base.exception.ErrorCode;

public class AdditionalInfoNotUpdatedException extends JwtAuthenticationException {
    public AdditionalInfoNotUpdatedException() {
        super(ErrorCode.ADDITIONAL_INFO_NOT_UPDATED);
    }
}
