package com.example.demo.global.base.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Getter;

@Getter
@JsonTypeName("true")
public final class SuccessResponseBody<T> extends ResponseBody<T> {
    private final T data;

    public SuccessResponseBody() {
        data = null;
    }

    public SuccessResponseBody(T result) {
        this.data = result;
    }
}
