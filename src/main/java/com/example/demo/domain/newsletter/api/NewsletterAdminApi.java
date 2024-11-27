package com.example.demo.domain.newsletter.api;

import com.example.demo.domain.newsletter.domain.dto.request.EmailNoticeRequest;
import com.example.demo.domain.newsletter.domain.dto.response.SeminarNoticeBasicForm;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface NewsletterAdminApi {
    @Operation(
            summary = "[ADMIN] 뉴스레터 전송 api",
            description = "공지사항 같은 손수 작성해 뉴스레터를 전송합니다."
    )
    @ApiResponse( content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    description = "ADMIN 사용자가 사용 가능합니다.\n"
                            + "공지사항 같은 손수 작성해 뉴스레터를 전송합니다.")
    )
    ResponseEntity<ResponseBody<Void>> sendNoticeByEmail(@Valid @RequestBody EmailNoticeRequest request);

    @Operation(
            summary = "[ADMIN] 뉴스레터 공지사항 폼 가져오기 api",
            description = "공지사항 폼을 받아옵니다."
    )
    @ApiResponse( content = @Content(mediaType = "application/json", schema = @Schema(implementation = SeminarNoticeBasicForm.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = SeminarNoticeBasicForm.class,
                    description = "ADMIN 사용자가 사용 가능합니다.\n"
                            + "공지사항 폼을 받아옵니다.")
    )
    ResponseEntity<ResponseBody<SeminarNoticeBasicForm>> getSeminarNoticeBasicForm();
}
