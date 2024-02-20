package com.example.demo.global.base.exception;

import com.example.demo.global.base.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String EXCEPTION_FORMAT = "[EXCEPTION]                   -----> ";
    private static final String EXCEPTION_MESSAGE_FORMAT = "[EXCEPTION] EXCEPTION_MESSAGE -----> [{}]";
    private static final String EXCEPTION_TYPE_FORMAT = "[EXCEPTION] EXCEPTION_TYPE    -----> [{}]";
    private static final String EXCEPTION_REQUEST_URI = "[EXCEPTION] REQUEST_URI       -----> [{}]";
    private static final String EXCEPTION_HTTP_METHOD_TYPE = "[EXCEPTION] HTTP_METHOD_TYPE  -----> [{}]";

    @ExceptionHandler(ServiceException.class) // custom 에러
    public ResponseEntity<ErrorResponse> handleServiceException(HttpServletRequest request, ServiceException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.info("에러코드 테스트: " + String.valueOf(errorCode));
        logService(request, e, errorCode);
        return ErrorResponse.of(errorCode);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.debug("[EXCEPTION] FIELD_ERROR       -----> [{}]", e.getFieldError());

        return ResponseEntity.
                status(BAD_REQUEST)
                .body(new ErrorResponse(errorMessage));
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MissingRequestValueException.class) // 요청 데이터로 들어와야할 인자 부족
    public ResponseEntity<ErrorResponse> handleMissingRequestValueException(HttpServletRequest request,
                                                            MissingRequestValueException e) {
        logDebug(request, e);
        return ErrorResponse.of(ErrorCode.MISSING_INPUT_VALUE);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class) // 해당 uri에 잘못된 HttpMethod
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpServletRequest request,
                                                                      HttpRequestMethodNotSupportedException e) {
        logDebug(request, e);
        return ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(NoHandlerFoundException.class) // 없는 api(uri)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(HttpServletRequest request, NoHandlerFoundException e) {
        logDebug(request, e);
        return ErrorResponse.of(ErrorCode.NOT_EXIST_API);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class) // 메소드 validation 예외 상황
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        logWarn(e);
        return ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE);
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(HttpServletRequest request, Exception e) {
        logError(e);
        return ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    private void logService(HttpServletRequest request, ServiceException e, ErrorCode errorCode) {
        log.debug(EXCEPTION_REQUEST_URI, request.getRequestURI());
        log.debug(EXCEPTION_HTTP_METHOD_TYPE, request.getMethod());
//        log.debug(MessageUtil.getMessage(e.getMessageKey()));
        log.warn(EXCEPTION_TYPE_FORMAT, e.getClass().getSimpleName());
        log.warn(EXCEPTION_MESSAGE_FORMAT, e.getMessage());
    }

    private void logDebug(HttpServletRequest request, Exception e) {
        log.debug(EXCEPTION_REQUEST_URI, request.getRequestURI());
        log.debug(EXCEPTION_HTTP_METHOD_TYPE, request.getMethod());
        log.debug(EXCEPTION_TYPE_FORMAT, e.getClass().getSimpleName());
        log.debug(EXCEPTION_MESSAGE_FORMAT, e.getMessage());
    }

    private void logWarn(Exception e) {
        log.warn(EXCEPTION_TYPE_FORMAT, e.getClass().getSimpleName());
        log.warn(EXCEPTION_MESSAGE_FORMAT, e.getMessage());
    }

    private void logError(Exception e) {
        log.error(EXCEPTION_TYPE_FORMAT, e.getClass().getSimpleName());
        log.error(EXCEPTION_MESSAGE_FORMAT, e.getMessage());
        log.error(EXCEPTION_FORMAT, e);
    }
}
