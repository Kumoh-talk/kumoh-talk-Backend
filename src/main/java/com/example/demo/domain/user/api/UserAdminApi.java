package com.example.demo.domain.user.api;

import com.example.demo.domain.user.domain.dto.request.UpdateUserInfoRequest;
import com.example.demo.domain.user.domain.dto.response.UserInfo;
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
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserAdminApi {
    @Operation(
            summary = "[ADMIN] 모든 사용자의 정보를 확인하는 api",
            description = "모든 사용자의 정보를 반환합니다."
    )
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalPageResponse.class))
    )
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserInfo.class))
    )
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = GlobalPageResponse.class,
                    description = "ADMIN 권한 사용자가 사용할 수 있습니다.\n"
                            + "모든 사용자의 정보를 반환합니다.")
    )
    ResponseEntity<ResponseBody<GlobalPageResponse<UserInfo>>> getAllUsers (
            @ParameterObject
            @PageableDefault(page=0, size=10,sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    );

    @Operation(
            summary = "[ADMIN] 사용자의 정보를 수정하는 api",
            description = "사용자의 정보를 수정합니다."
    )
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))
    )
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    description = "ADMIN 권한 사용자가 사용할 수 있습니다.\n"
                            + "사용자의 정보를 수정할 수 있습니다."),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.USER_NOT_FOUND),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.EXIST_SAME_NICKNAME)
            }
    )
    ResponseEntity<ResponseBody<Void>> updateUserInfo (@PathVariable Long userId,
                                                       @Valid @RequestBody UpdateUserInfoRequest request);

    @Operation(
            summary = "[ADMIN] 사용자를 삭제하는 api",
            description = "사용자를 삭제합니다."
    )
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))
    )
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    description = "ADMIN 권한 사용자가 사용할 수 있습니다.\n"
                            + "사용자의 정보를 삭제할 수 있습니다.")
    )
    ResponseEntity<ResponseBody<Void>> deleteUser (@PathVariable Long userId);
}
