package com.example.demo.domain.seminar_application.api;

import com.example.demo.domain.seminar_application.domain.dto.response.SeminarApplicationInfo;
import com.example.demo.global.base.dto.ResponseBody;
import com.example.demo.global.base.dto.page.GlobalPageResponse;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.config.swagger.ApiErrorResponseExplanation;
import com.example.demo.global.config.swagger.ApiResponseExplanations;
import com.example.demo.global.config.swagger.ApiSuccessResponseExplanation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;

public interface SeminarApplicationAdminApi {
    @Operation(
            summary = "[ADMIN] 모든 신청서를 확인하는 api",
            description = "모든 신청서를 반환합니다."
    )
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalPageResponse.class))
    )
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = SeminarApplicationInfo.class))
    )
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = GlobalPageResponse.class,
                    description = "ADMIN 이상의 권한 사용자가 사용할 수 있습니다.\n"
                            + "모든 신청서를 반환합니다.")
    )
    ResponseEntity<ResponseBody<GlobalPageResponse<SeminarApplicationInfo>>> getAllSeminarApplications (
            @ParameterObject
            @PageableDefault(page=0, size=10,sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    );
}
