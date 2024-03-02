package com.example.demo.global.base.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {
    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "잘못된 입력 값입니다."),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "잘못된 타입입니다."),
    MISSING_INPUT_VALUE(HttpStatus.BAD_REQUEST,  "인자가 부족합니다."),
    NOT_EXIST_API(HttpStatus.BAD_REQUEST, "요청 주소가 올바르지 않습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "사용할 수 없는 메서드입니다."),
    HANDLE_ACCESS_DENIED(HttpStatus.FORBIDDEN,  "접근 권한이 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러입니다."),

    // 400 error
    INVALID_JSON(HttpStatus.BAD_REQUEST, "JSON 파싱 오류입니다."),
    MISMATCH_EMAIL_OR_PASSWORD(HttpStatus.BAD_REQUEST, "이메일 혹은 비밀번호가 틀렸습니다."),
    MISMATCH_EMAIL_AUTH_CODE(HttpStatus.BAD_REQUEST, "인증 코드가 틀렸습니다."),

    // 401 error
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰의 유효성 혹은 형식이 올바르지 않습니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    NOT_AUTHORIZED_TOKEN(HttpStatus.UNAUTHORIZED, "권한 정보가 없는 토큰입니다."),

    // 404 error
    FAIL_USER_LOGIN(HttpStatus.NOT_FOUND, "존재하지 않는 계정입니다."),

    // 409 error
    EXIST_SAME_EMAIL(HttpStatus.CONFLICT, "이미 사용중인 이메일 입니다."),

    // 500 error
    UNABLE_TO_SEND_EMAIL(HttpStatus.INTERNAL_SERVER_ERROR, "이메일을 전송할 수 없습니다."),
    NO_SUCH_ALGORITHM(HttpStatus.INTERNAL_SERVER_ERROR, "해당 알고리즘을 찾을 수 없습니다."),
    UNABLE_TO_CREATE_AUTHENTICATION(HttpStatus.INTERNAL_SERVER_ERROR, "토큰에 정보가 부족하여 인증생성에 실패했습니다."),

    ;

    private final HttpStatus status;
    private final String message;
}
