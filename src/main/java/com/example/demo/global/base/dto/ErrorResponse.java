package com.example.demo.global.base.dto;

import com.example.demo.global.base.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorResponse{
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime timestamp;

    private String message;
    public static ResponseEntity<ErrorResponse> of(ErrorCode code) {
        return ResponseEntity
                .status(code.getStatus())
                .body(new ErrorResponse(LocalDateTime.now() ,code.getMessage()));
    }

    public static ResponseEntity<ErrorResponse> of(HttpStatus status, String message) {
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(LocalDateTime.now() , message));
    }
}
