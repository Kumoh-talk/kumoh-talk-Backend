package com.example.demo.domain.user.api;

import com.example.demo.domain.token.domain.dto.TokenResponse;
import com.example.demo.domain.user.domain.dto.request.CompleteRegistrationRequest;
import com.example.demo.domain.user.domain.dto.request.UpdateNicknameRequest;
import com.example.demo.domain.user.domain.dto.response.UserInfo;
import com.example.demo.domain.user.domain.dto.response.UserProfile;
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
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import static com.example.demo.global.regex.UserRegex.NICKNAME_REGEXP;

public interface UserApi {
    @Operation(
            summary = "GUEST 사용자에 한해서 닉네임 중복 여/부를 확인하는 api",
            description = "[GUEST] 닉네임 중복 여/부를 확인합니다."
    )
    @ApiResponse( content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    description = "GUEST 권한을 가진 사용자에 대해서만 사용할 수 있습니다.\n"
                            + "닉네임에 대한 검증과 중복 여/부를 검사합니다."),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.EXIST_SAME_NICKNAME),
            }
    )
    ResponseEntity<ResponseBody<Void>> checkNicknameDuplicate(
            @Parameter(description = "닉네임", required = true)
            @Pattern(regexp = NICKNAME_REGEXP, message = "닉네임 정규식을 맞춰주세요.") String nickname);

    @Operation(
            summary = "GUEST 사용자에 한해서 초기 추가정보를 입력받는 api",
            description = "[GUEST] 초기 추가정보를 입력받고 GUEST -> USER 로 권한이 증가합니다."
    )
    @ApiResponse( content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenResponse.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = TokenResponse.class,
                    description = "GUEST 권한을 가진 사용자에 대해서만 사용할 수 있습니다.\n"
                            + "초기 추가정보를 입력받고 GUEST -> USER 로 권한이 증가합니다."),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.USER_NOT_FOUND),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.EXIST_SAME_NICKNAME),
            }
    )
    ResponseEntity<ResponseBody<TokenResponse>> completeRegistration(@RequestBody @Valid CompleteRegistrationRequest request,
                                                                     @Parameter(hidden = true) Long userId);

    @Operation(
            summary = "로그아웃 api",
            description = "로그인된 사용자에 대해서 로그아웃합니다."
    )
    @ApiResponse( content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    description = "USER 권한 이상을 가진 사용자에 대해서만 사용할 수 있습니다.\n"
                            + "로그아웃을 진행합니다.")
    )
    ResponseEntity<ResponseBody<Void>> logout(@Parameter(hidden = true) Long userId);

    @Operation(
            summary = "닉네임 변경 api",
            description = "USER 이상의 권한에 대해서 닉네임을 수정합니다."
    )
    @ApiResponse( content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    description = "USER 이상의 권한을 가진 사용자에 대해서만 사용할 수 있습니다.\n"
                            + "닉네임을 수정합니다."),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.USER_NOT_FOUND),
            }
    )
    ResponseEntity<ResponseBody<Void>> updateNickname(@RequestBody @Valid UpdateNicknameRequest request,
                                                      @Parameter(hidden = true) Long userId);

    @Operation(
            summary = "내 정보 확인 api",
            description = "USER 이상의 권한에 대해서 자신의 사용자 정보를 확인합니다."
    )
    @ApiResponse( content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserInfo.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = UserInfo.class,
                    description = "USER 이상의 권한을 가진 사용자에 대해서만 사용할 수 있습니다.\n"
                            + "내 기본 정보를 확인할 수 있습니다."),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.USER_NOT_FOUND),
            }
    )
    ResponseEntity<ResponseBody<UserInfo>> getUserInfo(@Parameter(hidden = true) Long userId);

    @Operation(
            summary = "내 프로필 정보 확인 api",
            description = "모든 사용자 대해서 다른 사용자의 프로필 정보를 확인합니다."
    )
    @ApiResponse( content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfile.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = UserProfile.class,
                    description = "모든 사용자가 사용할 수 있습니다.\n"
                            + "내 기본 프로필 정보를 확인할 수 있습니다."),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.USER_NOT_FOUND),
            }
    )
    ResponseEntity<ResponseBody<UserProfile>> getUserProfile(@PathVariable Long userId);
}
