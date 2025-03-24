package com.example.demo.application.newsletter.api;

import com.example.demo.application.newsletter.dto.request.NewsletterSubscribeRequest;
import com.example.demo.application.newsletter.dto.request.NewsletterUpdateNotifyRequest;
import com.example.demo.global.base.dto.ResponseBody;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.config.swagger.ApiErrorResponseExplanation;
import com.example.demo.global.config.swagger.ApiResponseExplanations;
import com.example.demo.global.config.swagger.ApiSuccessResponseExplanation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import static com.example.demo.global.regex.UserRegex.EMAIL_REGEXP;

public interface NewsletterApi {
    @Operation(
            summary = "모든 사용자에 대해 뉴스레터를 구독하는 api",
            description = "뉴스레터를 구독합니다."
    )
    @ApiResponse( content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    description = "모든 사용자가 사용 가능합니다.\n"
                            + "뉴스레터를 구독합니다."),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.SUBSCRIBE_EMAIL_CONFLICT),
            }
    )
    ResponseEntity<ResponseBody<Void>> subscribe(@RequestBody @Valid NewsletterSubscribeRequest request);

    @Operation(
            summary = "모든 사용자에 대해 뉴스레터를 수정하는 api",
            description = "뉴스레터를 수정합니다."
    )
    @ApiResponse( content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    description = "모든 사용자가 사용 가능합니다.\n"
                            + "뉴스레터를 수정합니다."),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.SUBSCRIBE_NOT_FOUND),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.SUBSCRIBE_EMAIL_CONFLICT),
            }
    )
    ResponseEntity<ResponseBody<Void>> updateNewsletterNotify(@RequestBody @Valid NewsletterUpdateNotifyRequest request);

    @Operation(
            summary = "모든 사용자에 대해 뉴스레터를 삭제하는 api",
            description = "뉴스레터를 구독 취소합니다."
    )
    @ApiResponse( content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    description = "모든 사용자가 사용 가능합니다.\n"
                            + "뉴스레터를 구독 취소합니다."),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.SUBSCRIBE_NOT_FOUND),
            }
    )
    ResponseEntity<ResponseBody<Void>> deleteNewsletterInfo(@RequestParam @Pattern(regexp = EMAIL_REGEXP, message = "이메일 정규식을 맞춰주세요.") String email);
}
