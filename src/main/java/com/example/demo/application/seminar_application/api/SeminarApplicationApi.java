package com.example.demo.application.seminar_application.api;

import com.example.demo.application.seminar_application.dto.request.SeminarApplicationRequest;
import com.example.demo.application.seminar_application.dto.request.SeminarApplicationUpdateRequest;
import com.example.demo.application.seminar_application.dto.response.SeminarApplicationResponse;
import com.example.demo.application.token.dto.TokenResponse;
import com.example.demo.global.base.dto.ResponseBody;
import com.example.demo.global.base.dto.page.GlobalPageResponse;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.config.swagger.ApiErrorResponseExplanation;
import com.example.demo.global.config.swagger.ApiResponseExplanations;
import com.example.demo.global.config.swagger.ApiSuccessResponseExplanation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface SeminarApplicationApi {
    @Operation(
            summary = "세미나 신청서 작성 api",
            description = "[ROLE_ACTIVE_USER] 이상의 권한을 가진 사용자들에 대해서 가능합니다."
    )
    @ApiResponse( content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenResponse.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = TokenResponse.class,
                    description = "ROLE_ACTIVE_USER 이상의 권한을 가진 사용자에 대해서만 사용할 수 있습니다.\n"
                            + "세미나 신청서를 작성합니다. 첫 신청이라면 권한이 증가된 토큰을 발급하고, 처음이 아니라면 204 status code로 반환됩니다."),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.USER_NOT_FOUND),
            }
    )
    ResponseEntity<ResponseBody<TokenResponse>> applyForSeminar(@Parameter(hidden = true) Long userId,
                                                                @RequestBody @Valid SeminarApplicationRequest request);

    @Operation(
            summary = "내가 작성한 모든 신청서를 확인하는 api",
            description = "내가 작성한 모든 신청서를 반환합니다."
    )
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalPageResponse.class))
    )
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SeminarApplicationResponse.class))
    )
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = GlobalPageResponse.class,
                    description = "ROLE_ACTIVE_USER 이상의 권한 사용자가 사용할 수 있습니다.\n"
                            + "내가 작성한 모든 신청서를 반환합니다."),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.USER_NOT_FOUND),
            }
    )
    ResponseEntity<ResponseBody<GlobalPageResponse<SeminarApplicationResponse>>> getSeminarApplicationByUserId(
            @Parameter(hidden = true) Long userId,
            @ParameterObject
            @PageableDefault(page=0, size=10,sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    );

    @Operation(
            summary = "내 세미나 신청서 수정 api",
            description = "[ROLE_ACTIVE_USER] 이상의 권한을 가진 사용자들에 대해서 가능합니다."
    )
    @ApiResponse( content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    description = "ROLE_ACTIVE_USER 이상의 권한을 가진 사용자에 대해서만 사용할 수 있습니다.\n"
                            + "내가 쓴 세미나 신청서를 수정합니다. 발표 하루전이라면 수정/삭제가 불가능합니다."),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.SEMINAR_APPLICATION_NOT_FOUND),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.SEMINAR_APPLICATION_CANNOT_EDIT_OR_DELETE),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.SEMINAR_APPLICATION_ACCESS_DENIED),
            }
    )
    ResponseEntity<ResponseBody<Void>> updateSeminarApplication(@Parameter(hidden = true) Long userId,
                                                                @PathVariable Long seminarApplicationId,
                                                                @RequestBody @Valid SeminarApplicationUpdateRequest request);

    @Operation(
            summary = "내 세미나 신청서 삭제 api",
            description = "[ROLE_ACTIVE_USER] 이상의 권한을 가진 사용자들에 대해서 가능합니다."
    )
    @ApiResponse( content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    description = "ROLE_ACTIVE_USER 이상의 권한을 가진 사용자에 대해서만 사용할 수 있습니다.\n"
                            + "내가 쓴 세미나 신청서를 삭제합니다. 발표 하루전이라면 수정/삭제가 불가능합니다."),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.SEMINAR_APPLICATION_NOT_FOUND),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.SEMINAR_APPLICATION_CANNOT_EDIT_OR_DELETE),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.SEMINAR_APPLICATION_ACCESS_DENIED),
            }
    )
    ResponseEntity<ResponseBody<Void>> deleteSeminarApplication(@Parameter(hidden = true) Long userId,
                                                                @PathVariable Long seminarApplicationId);
}
