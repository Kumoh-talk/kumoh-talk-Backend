package com.example.demo.application.recruitment_application.api;

import com.example.demo.application.recruitment_application.dto.response.RecruitmentApplicantInfoResponse;
import com.example.demo.application.recruitment_application.dto.response.RecruitmentApplicationResponse;
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
import org.springframework.web.bind.annotation.PathVariable;

public interface AdminRecruitmentApplicationApi {
    @Operation(
            summary = "관리자 전용 모집 게시물의 신청인 페이지 조회",
            description = "관리자가 recruitmentBoardId에 해당하는 모집 게시물의 신청인 페이지를 조회합니다."
    )
    @ApiResponse(content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = RecruitmentApplicantInfoResponse.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = GlobalPageResponse.class,
                    description = "관리자의 신청인 페이지 조회 성공"),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.BOARD_NOT_FOUND),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.ACCESS_DENIED)
            }
    )
    ResponseEntity<ResponseBody<GlobalPageResponse<RecruitmentApplicantInfoResponse>>> getApplicantListByAdmin(
            @ParameterObject @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable Long recruitmentBoardId);

    @Operation(
            summary = "관리자 전용 신청서 상세 조회",
            description = "관리자가 applicantId에 해당하는 신청서를 상세 조회합니다."
    )
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecruitmentApplicationResponse.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = RecruitmentApplicationResponse.class,
                    description = "관리자의 신청서 상세 조회 성공"),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.BOARD_NOT_FOUND),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.ACCESS_DENIED),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.RECRUITMENT_APPLICANT_NOT_FOUND)
            }
    )
    ResponseEntity<ResponseBody<RecruitmentApplicationResponse>> getApplicationInfoByApplicantIdByAdmin(
            @PathVariable Long recruitmentBoardId,
            @PathVariable Long applicantId);
}
