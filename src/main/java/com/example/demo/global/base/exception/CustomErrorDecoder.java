package com.example.demo.global.base.exception;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import static com.example.demo.global.base.exception.ErrorCode.INTERNAL_SERVER_ERROR;

@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(final String methodKey, final Response response) {
        log.warn("statusCode[{}], methodKey[{}]", response.status(), methodKey);

        return switch (response.status()) {
            // TODO. 추가로 필요하다면 처리
            default -> new ServiceException(INTERNAL_SERVER_ERROR);
        };
    }
}