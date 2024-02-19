package com.example.demo.global.base.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonPropertyOrder({"success", "data"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private final Boolean success;
    private final T data;

    public static ApiResponse<Void> ok(Boolean success) {
        return new ApiResponse<>(success, null);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data);
    }

    public static <T> ApiResponse<T> ok(Boolean success, T data) {
        return new ApiResponse<>(success, data);
    }

    public static ApiResponse<Void> noData() {
        return new ApiResponse<>(true, null);
    }
}
