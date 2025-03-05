package com.example.demo.domain.recruitment_application.controller.swagger;

import com.example.demo.domain.recruitment_application.domain.dto.request.RecruitmentApplicationRequest;
import com.example.demo.domain.recruitment_application.domain.dto.response.MyRecruitmentApplicationPageResponse;
import com.example.demo.domain.recruitment_application.domain.dto.response.RecruitmentApplicantPageResponse;
import com.example.demo.domain.recruitment_application.domain.dto.response.RecruitmentApplicationResponse;
import com.example.demo.domain.recruitment_board.entity.vo.RecruitmentBoardType;
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
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface RecruitmentApplicationApi {

    @Operation(
            summary = "모집 신청 저장",
            description = "recruitmentBoardId에 해당하는 모집 게시물에 신청을 저장합니다. \n" +
                    "마감 기한 이전에 신청이 가능합니다."
    )
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecruitmentApplicationResponse.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = RecruitmentApplicationResponse.class,
                    description = "신청 성공"),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.USER_NOT_FOUND),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.BOARD_NOT_FOUND),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.DEADLINE_EXPIRED),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.OMIT_ESSENTIAL_QUESTION),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.RECRUITMENT_APPLICANT_EXIST),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.ANSWER_NOT_FOUND),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.INVALID_INPUT_VALUE)
            }
    )
    ResponseEntity<ResponseBody<RecruitmentApplicationResponse>> createApplication(
            @Parameter(hidden = true) Long userId,
            @PathVariable Long recruitmentBoardId,
            @RequestBody @Valid RecruitmentApplicationRequest request);

    @Operation(
            summary = "모집 게시물의 신청인 페이지 조회",
            description = "recruitmentBoardId에 해당하는 모집 게시물의 신청인 페이지를 조회합니다."
    )
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecruitmentApplicantPageResponse.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = RecruitmentApplicantPageResponse.class,
                    description = "신청인 페이지 조회 성공"),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.BOARD_NOT_FOUND),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.ACCESS_DENIED)
            }
    )
    ResponseEntity<ResponseBody<RecruitmentApplicantPageResponse>> getApplicantList(
            @Parameter(hidden = true) Long userId,
            @PathVariable Long recruitmentBoardId,
            @ParameterObject @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable);

    @Operation(
            summary = "신청서 상세 조회",
            description = "applicantId에 해당하는 신청서를 상세 조회합니다."
    )
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecruitmentApplicationResponse.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = RecruitmentApplicationResponse.class,
                    description = "신청서 상세 조회 성공"),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.BOARD_NOT_FOUND),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.ACCESS_DENIED),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.RECRUITMENT_APPLICANT_NOT_FOUND)
            }
    )
    ResponseEntity<ResponseBody<RecruitmentApplicationResponse>> getApplicationInfoByApplicantId(
            @Parameter(hidden = true) Long userId,
            @PathVariable Long recruitmentBoardId,
            @PathVariable Long applicantId);

    @Operation(
            summary = "신청서 수정",
            description = "applicantId에 해당하는 신청서를 수정합니다.\n" +
                    "마감기한 이전에 수정이 가능합니다."
    )
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecruitmentApplicationResponse.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = RecruitmentApplicationResponse.class,
                    description = "신청서 수정 성공"),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.USER_NOT_FOUND),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.RECRUITMENT_APPLICANT_NOT_FOUND),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.ACCESS_DENIED),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.BOARD_NOT_FOUND),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.DEADLINE_EXPIRED),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.OMIT_ESSENTIAL_QUESTION),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.QUESTION_NOT_FOUND),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.ANSWER_NOT_FOUND),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.INVALID_INPUT_VALUE)
            }
    )
    ResponseEntity<ResponseBody<RecruitmentApplicationResponse>> updateApplication(
            @Parameter(hidden = true) Long userId,
            @PathVariable Long applicantId,
            @RequestBody @Valid RecruitmentApplicationRequest request);

    @Operation(
            summary = "신청서 삭제",
            description = "applicantId에 해당하는 신청서를 삭제합니다.\n" +
                    "마감 기한 이전에 삭제가 가능합니다."
    )
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    description = "신청서 삭제 성공"),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.RECRUITMENT_APPLICANT_NOT_FOUND),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.ACCESS_DENIED)
            }
    )
    ResponseEntity<ResponseBody<Void>> deleteApplication(
            @Parameter(hidden = true) Long userId,
            @PathVariable Long applicantId);

    @Operation(
            summary = "페이지 번호 방식 사용자 신청서 페이지 조회",
            description = "페이지 번호를 통해 사용자의 신청서 페이지를 조회합니다."
    )
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = MyRecruitmentApplicationPageResponse.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = MyRecruitmentApplicationPageResponse.class,
                    description = "신청서 페이지 조회 성공"),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.USER_NOT_FOUND)
            }
    )
    ResponseEntity<ResponseBody<MyRecruitmentApplicationPageResponse>> getUserApplicationList(
            @Parameter(hidden = true) Long userId,
            @Parameter(description = "모집 게시물 타입(study, project, mentoring)", example = "study") @RequestParam RecruitmentBoardType recruitmentBoardType,
            @ParameterObject @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable);
}
