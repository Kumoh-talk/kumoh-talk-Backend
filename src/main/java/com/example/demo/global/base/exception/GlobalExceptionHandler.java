package com.example.demo.global.base.exception;

import com.example.demo.global.base.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class) // custom 에러
    public ResponseEntity<ErrorResponse> handleServiceException(HttpServletRequest request, ServiceException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode,null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        log.error("MethodArgumentNotValidException : {}", errorMessage);
        return ResponseEntity.badRequest()
                .body(ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, errorMessage));
    }

    @ExceptionHandler(HandlerMethodValidationException.class) // HandlerMethodValidationException
    public ResponseEntity<ErrorResponse> handleMissingRequestValueException(HandlerMethodValidationException e) {
        String errorMessage = (String) Objects.requireNonNull(e.getDetailMessageArguments())[0];
        log.error("HandlerMethodValidationException : {}", e.getMessage());
        return ResponseEntity.badRequest()
            .body(ErrorResponse.of(ErrorCode.MISSING_INPUT_VALUE, errorMessage));
    }

    @ExceptionHandler(MissingRequestValueException.class) // 요청 데이터로 들어와야할 인자 부족
    public ResponseEntity<ErrorResponse> handleMissingRequestValueException(HttpServletRequest request,
                                                            MissingRequestValueException e) {
        log.error("MissingRequestValueException : {}", e.getMessage());
        return ResponseEntity.badRequest()
                .body(ErrorResponse.of(ErrorCode.MISSING_INPUT_VALUE, null));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class) // 해당 uri에 잘못된 HttpMethod
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpServletRequest request,
                                                                      HttpRequestMethodNotSupportedException e) {
        log.error("HttpRequestMethodNotSupportedException : {}", e.getMessage());
        return ResponseEntity.badRequest()
                .body(ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED, null));
    }

    @ExceptionHandler(NoHandlerFoundException.class) // 없는 api(uri)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(HttpServletRequest request, NoHandlerFoundException e) {
        log.error("NoHandlerFoundException : {}", e.getMessage());
        return ResponseEntity.badRequest()
                .body(ErrorResponse.of(ErrorCode.NOT_EXIST_API, null));
    }

    @ExceptionHandler(IllegalArgumentException.class) // 메소드 validation 예외 상황
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("IllegalArgumentException : {}", e.getMessage());
        return ResponseEntity.badRequest()
                .body(ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, null));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class) // JSON 파싱 예외
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("HttpMessageNotReadableException : {}", e.getMessage());
        return ResponseEntity.badRequest()
                .body(ErrorResponse.of(ErrorCode.INVALID_JSON, null));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class) // 쿼리 파라미터 형식 매칭 실패 예외
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("MethodArgumentTypeMismatchException : {}", e.getMessage());
        return ResponseEntity.badRequest()
                .body(ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, null));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class) // 파일 용량 제한 예외
    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.error("MaxUploadSizeExceededException : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(ErrorResponse.of(ErrorCode.FILE_TOO_LARGE, null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(HttpServletRequest request, Exception e) {
        log.error("Exception : {}", e.getMessage());
        return ResponseEntity.internalServerError()
                .body(ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR, null));
    }
}
