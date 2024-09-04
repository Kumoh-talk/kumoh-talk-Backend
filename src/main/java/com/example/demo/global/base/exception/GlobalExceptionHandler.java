package com.example.demo.global.base.exception;

import com.example.demo.global.base.dto.ResponseBody;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Objects;

import static com.example.demo.global.base.dto.ResponseUtil.createFailureResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class) // custom 에러
    public ResponseEntity<ResponseBody<Void>> handleServiceException(HttpServletRequest request, ServiceException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus())
                .body(createFailureResponse(errorCode));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class) // Valid
    public ResponseEntity<ResponseBody<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        log.error("MethodArgumentNotValidException : {}", errorMessage);
        return ResponseEntity.badRequest()
                .body(createFailureResponse(ErrorCode.INVALID_INPUT_VALUE, errorMessage));
    }

    @ExceptionHandler(HandlerMethodValidationException.class) // Valid 로 잡히지 않는 나머지 예외처리 (ex RequestParam)
    public ResponseEntity<ResponseBody<Void>> handleMissingRequestValueException(HandlerMethodValidationException e) {
        String errorMessage = (String) Objects.requireNonNull(e.getDetailMessageArguments())[0];
        log.error("HandlerMethodValidationException : {}", e.getMessage());
        return ResponseEntity.badRequest()
                .body(createFailureResponse(ErrorCode.MISSING_INPUT_VALUE, errorMessage));
    }

    @ExceptionHandler(MissingRequestValueException.class) // 요청 데이터로 들어와야할 인자 부족
    public ResponseEntity<ResponseBody<Void>> handleMissingRequestValueException(HttpServletRequest request,
                                                                                 MissingRequestValueException e) {
        log.error("MissingRequestValueException : {}", e.getMessage());
        return ResponseEntity.badRequest()
                .body(createFailureResponse(ErrorCode.MISSING_INPUT_VALUE));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class) // 해당 uri에 잘못된 HttpMethod
    public ResponseEntity<ResponseBody<Void>> handleHttpRequestMethodNotSupportedException(HttpServletRequest request,
                                                                                           HttpRequestMethodNotSupportedException e) {
        log.error("HttpRequestMethodNotSupportedException : {}", e.getMessage());
        return ResponseEntity.badRequest()
                .body(createFailureResponse(ErrorCode.METHOD_NOT_ALLOWED));
    }

    @ExceptionHandler(NoHandlerFoundException.class) // 없는 api(uri)
    public ResponseEntity<ResponseBody<Void>> handleNoHandlerFoundException(HttpServletRequest request, NoHandlerFoundException e) {
        log.error("NoHandlerFoundException : {}", e.getMessage());
        return ResponseEntity.badRequest()
                .body(createFailureResponse(ErrorCode.NOT_EXIST_API));
    }

    @ExceptionHandler(IllegalArgumentException.class) // 메소드 validation 예외 상황
    public ResponseEntity<ResponseBody<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("IllegalArgumentException : {}", e.getMessage());
        return ResponseEntity.badRequest()
                .body(createFailureResponse(ErrorCode.INVALID_INPUT_VALUE));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class) // JSON 파싱 예외
    public ResponseEntity<ResponseBody<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("HttpMessageNotReadableException : {}", e.getMessage());
        return ResponseEntity.badRequest()
                .body(createFailureResponse(ErrorCode.INVALID_JSON));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class) // 쿼리 파라미터 enum convert 실패 예외
    public ResponseEntity<?> converterExceptionHandler(MissingServletRequestParameterException e) {
        log.error("MissingServletRequestParameterException : {}", e.getMessage());
        return ResponseEntity.badRequest()
                .body(createFailureResponse(ErrorCode.REQUEST_PARAM_MISMATCH));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class) // 쿼리 파라미터 형식 매칭 실패 예외
    public ResponseEntity<ResponseBody<Void>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("MethodArgumentTypeMismatchException : {}", e.getMessage());
        return ResponseEntity.badRequest()
                .body(createFailureResponse(ErrorCode.INVALID_INPUT_VALUE));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class) // 파일 용량 제한 예외
    public ResponseEntity<ResponseBody<Void>> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.error("MaxUploadSizeExceededException : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(createFailureResponse(ErrorCode.FILE_TOO_LARGE));
    }

    @ExceptionHandler(AccessDeniedException.class) // 403 권한 관련 예외
    public ResponseEntity<ResponseBody<Void>> handleAccessDeniedException(AccessDeniedException e) {
        log.error("AccessDeniedException : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(createFailureResponse(ErrorCode.ACCESS_DENIED));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseBody<Void>> handleException(HttpServletRequest request, Exception e) {
        log.error("Exception : {}", e.getMessage());
        return ResponseEntity.internalServerError()
                .body(createFailureResponse(ErrorCode.INTERNAL_SERVER_ERROR));
    }
}
