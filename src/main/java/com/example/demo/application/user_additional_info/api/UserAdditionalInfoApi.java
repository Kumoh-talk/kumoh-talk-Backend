package com.example.demo.application.user_additional_info.api;


import com.example.demo.application.token.dto.TokenResponse;
import com.example.demo.application.user_additional_info.dto.request.CreateUserAdditionalInfoRequest;
import com.example.demo.application.user_additional_info.dto.request.UpdateUserAcademicInfoRequest;
import com.example.demo.application.user_additional_info.dto.response.UserAdditionalInfoResponse;
import com.example.demo.global.base.dto.ResponseBody;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserAdditionalInfoApi {
    @Operation(
            summary = "내 추가 정보 확인 api",
            description = "USER 권한 이상의 사용자가 내 추가 정보를 확인합니다."
    )
    @ApiResponse( content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserAdditionalInfoResponse.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = UserAdditionalInfoResponse.class,
                    description = "USER 권한 이상의 사용자가 사용할 수 있습니다.\n"
                            + "내 추가 정보를 확인할 수 있습니다."),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.USER_NOT_FOUND),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.USER_ADDITIONAL_INFO_NOT_FOUND),
            }
    )
    ResponseEntity<ResponseBody<UserAdditionalInfoResponse>> getUserAdditionalInfo(@Parameter(hidden = true) Long userId);

    @Operation(
            summary = "사용자 추가 정보 생성 api",
            description = "USER 권한 이상의 사용자가 내 추가 정보를 생성합니다."
    )
    @ApiResponse( content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenResponse.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = TokenResponse.class,
                    description = "USER 권한 이상의 사용자가 사용할 수 있습니다.\n"
                            + "내 추가 정보를 생성할 수 있습니다. 생성에 성공하면, 권한이 증가된 새로운 토큰을 발급합니다."),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.USER_NOT_FOUND),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.USER_ADDITIONAL_INFO_NOT_FOUND),
            }
    )
    ResponseEntity<ResponseBody<TokenResponse>> createUserAdditionalInfo(@Parameter(hidden = true) Long userId,
                                                                         @RequestBody @Valid CreateUserAdditionalInfoRequest request);

    @Operation(
            summary = "매 학기마다 진행되는 학적 정보 변경 API",
            description = "USER 권한 이상의 사용자가 학적 정보를 변경합니다."
    )
    @ApiResponse( content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    description = "USER 권한 이상의 사용자가 사용할 수 있습니다.\n"
                            + "학적 정보를 변경합니다. 성공적으로 변경된다면 서비스를 이용할 수 있습니다."),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.USER_NOT_FOUND),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.USER_ADDITIONAL_INFO_NOT_FOUND),
            }
    )
    ResponseEntity<ResponseBody<Void>> updateUserAcademicInfo(@Parameter(hidden = true) Long userId,
                                                                     @RequestBody @Valid UpdateUserAcademicInfoRequest request);
}
