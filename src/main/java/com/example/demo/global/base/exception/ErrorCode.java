package com.example.demo.global.base.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "COMMON_0001", "잘못된 입력 값입니다."),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "COMMON_0002", "잘못된 타입입니다."),
    MISSING_INPUT_VALUE(HttpStatus.BAD_REQUEST,"COMMON_0003", "인자가 부족합니다."),
    NOT_EXIST_API(HttpStatus.BAD_REQUEST, "COMMON_0004", "요청 주소가 올바르지 않습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "COMMON_0005", "사용할 수 없는 메서드입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_0006", "서버 에러입니다."),
    INVALID_JSON(HttpStatus.BAD_REQUEST, "COMMON_0007", "JSON 파싱 오류입니다."),
    NEED_AUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON_0008","인증이 필요합니다."),


    // User
    MISMATCH_EMAIL_OR_PASSWORD(HttpStatus.BAD_REQUEST, "USER_0001", "이메일 혹은 비밀번호가 틀렸습니다."),
    MISMATCH_EMAIL_AUTH_CODE(HttpStatus.BAD_REQUEST, "USER_0002", "인증 코드가 틀렸습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "USER_0003", "비밀번호가 일치하지 않습니다."),
    FAIL_USER_LOGIN(HttpStatus.NOT_FOUND, "USER_0004", "존재하지 않는 계정입니다."),
    EXIST_SAME_EMAIL(HttpStatus.CONFLICT, "USER_0005", "이미 사용중인 이메일 입니다."),
    UNABLE_TO_SEND_EMAIL(HttpStatus.INTERNAL_SERVER_ERROR, "USER_0006","이메일을 전송할 수 없습니다."),
    NO_SUCH_ALGORITHM(HttpStatus.INTERNAL_SERVER_ERROR, "USER_0007","해당 알고리즘을 찾을 수 없습니다."),

    // Board
    NOT_ACCESS_USER(HttpStatus.UNAUTHORIZED, "BOARD_0001", "해당 유저가 접근할 수 없는 게시물입니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "BOARD_0002", "존재하지 않는 게시물입니다." ),

    // FILE
    FILE_TOO_LARGE(HttpStatus.PAYLOAD_TOO_LARGE, "FILE_0001", "파일의 용량이 너무 큽니다."),

    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
