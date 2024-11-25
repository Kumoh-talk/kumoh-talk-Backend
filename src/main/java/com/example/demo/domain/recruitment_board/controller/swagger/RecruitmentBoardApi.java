package com.example.demo.domain.recruitment_board.controller.swagger;

import com.example.demo.domain.board.domain.dto.vo.Status;
import com.example.demo.domain.recruitment_board.domain.dto.request.RecruitmentBoardInfoAndFormRequest;
import com.example.demo.domain.recruitment_board.domain.dto.response.*;
import com.example.demo.domain.recruitment_board.domain.vo.RecruitmentBoardType;
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
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface RecruitmentBoardApi {

    @Operation(
            summary = "모집 게시물 및 신청폼 저장",
            description = "모집 게시물 및 신청폼을 저장합니다.\n" +
                    "임시저장에서 불러온 게시물을 published로 저장하고 싶을 때는 저장이 아닌 수정으로 요청을 전송해야합니다."
    )
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecruitmentBoardInfoAndFormResponse.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = RecruitmentBoardInfoAndFormResponse.class,
                    description = "모집 게시물 및 신청폼 저장 성공"),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.USER_NOT_FOUND),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.INVALID_INPUT_VALUE)
            }
    )
    ResponseEntity<ResponseBody<RecruitmentBoardInfoAndFormResponse>> createRecruitmentBoardAndForm(
            @Parameter(hidden = true) Long userId,
            @Parameter(description = "게시물 상태(draft, published)", example = "published") @RequestParam Status status,
            @RequestBody RecruitmentBoardInfoAndFormRequest recruitmentBoardInfoAndFormRequest) throws MethodArgumentNotValidException;

    @Operation(
            summary = "no-offset 방식 모집 게시물 페이지 조회",
            description = "무한스크롤이 필요한 홈 화면에서 출력될 모집 게시물 페이지를 조회합니다."
    )
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecruitmentBoardNoOffsetResponse.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = RecruitmentBoardNoOffsetResponse.class,
                    description = "모집 게시물 페이지 조회 성공")
    )
    ResponseEntity<ResponseBody<RecruitmentBoardNoOffsetResponse>> getRecruitmentBoardListByNoOffset(
            @Parameter(description = "페이지 크기", example = "10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "이전 페이지의 가장 마지막 게시물 id(첫 페이지 조회 시 입력 x)", example = "11") @RequestParam(required = false) Long lastBoardId,
            @Parameter(description = "모집 게시물 타입(study, project, mentoring)", example = "study") @RequestParam RecruitmentBoardType recruitmentBoardType
    );

    @Operation(
            summary = "페이지 번호 방식 모집 게시물 페이지 조회",
            description = "페이지 번호가 필요한 모집 게시물 더보기 화면에서 출력될 모집 게시물 페이지를 조회합니다."
    )
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecruitmentBoardPageNumResponse.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = RecruitmentBoardPageNumResponse.class,
                    description = "모집 게시물 페이지 조회 성공")
    )
    ResponseEntity<ResponseBody<RecruitmentBoardPageNumResponse>> getRecruitmentBoardListByPageNum(
            @ParameterObject @PageableDefault(page = 0, size = 10, sort = "recruitmentDeadline", direction = Sort.Direction.ASC) Pageable pageable,
            @Parameter(description = "모집 게시물 타입(study, project, mentoring)", example = "study") @RequestParam RecruitmentBoardType recruitmentBoardType
    );

    @Operation(
            summary = "모집 게시물 정보 상세조회",
            description = "recruitmentBoardId에 해당하는 모집 게시물 정보를 상세조회 합니다."
    )
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecruitmentBoardInfoResponse.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = RecruitmentBoardInfoResponse.class,
                    description = "모집 게시물 정보 상세조회 성공"),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.BOARD_NOT_FOUND),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.DRAFT_NOT_ACCESS_USER)

            }
    )
    ResponseEntity<ResponseBody<RecruitmentBoardInfoResponse>> getRecruitmentBoardInfo(
            @Parameter(hidden = true) Long userId, @PathVariable Long recruitmentBoardId);

    @Operation(
            summary = "모집 게시물 신청폼 상세조회",
            description = "recruitmentBoardId에 해당하는 모집 게시물 신청폼을 상세조회 합니다."
    )
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecruitmentFormQuestionResponse.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = RecruitmentFormQuestionResponse.class,
                    description = "모집 게시물 신청폼 상세조회 성공"),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.BOARD_NOT_FOUND),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.ACCESS_DENIED),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.DRAFT_NOT_ACCESS_USER)
            }
    )
    ResponseEntity<ResponseBody<List<RecruitmentFormQuestionResponse>>> getRecruitmentFormInfo(
            @Parameter(hidden = true) Long userId, @PathVariable Long recruitmentBoardId);

    @Operation(
            summary = "모집 게시물 정보 및 신청폼 수정",
            description = "recruitmentBoardId에 해당하는 모집 게시물 정보 및 신청폼을 수정합니다.\n" +
                    "만약 임시저장된 게시물을 불러온 후 그 게시물을 저장한다면 저장 api가 아닌 수정 api를 요청해야합니다."
    )
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecruitmentBoardInfoAndFormResponse.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = RecruitmentBoardInfoAndFormResponse.class,
                    description = "모집 게시물 정보 및 신청폼 수정 성공"),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.BOARD_NOT_FOUND),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.ACCESS_DENIED),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.RECRUITMENT_APPLICANT_EXIST),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.INVALID_INPUT_VALUE)
            }
    )
    ResponseEntity<ResponseBody<RecruitmentBoardInfoAndFormResponse>> updateRecruitmentBoardAndForm(
            @Parameter(hidden = true) Long userId,
            @PathVariable Long recruitmentBoardId,
            @Parameter(description = "게시물 상태(draft, published)", example = "published") @RequestParam Status status,
            @RequestBody RecruitmentBoardInfoAndFormRequest recruitmentBoardInfoAndFormRequest) throws MethodArgumentNotValidException;

    @Operation(
            summary = "모집 게시물 정보 및 신청폼 삭제",
            description = "recruitmentBoardId에 해당하는 모집 게시물 정보 및 신청폼을 삭제합니다."
    )
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    description = "모집 게시물 정보 및 신청폼 삭제 성공"),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.BOARD_NOT_FOUND),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.ACCESS_DENIED)
            }
    )
    ResponseEntity<ResponseBody<Void>> deleteRecruitmentBoardAndForm(
            @Parameter(hidden = true) Long userId,
            @PathVariable Long recruitmentBoardId);

    @Operation(
            summary = "사용자의 최근 임시저장 모집 게시물 조회",
            description = "사용자가 최근에 임시저장한 모집 게시물을 조회합니다."
    )
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecruitmentBoardInfoAndFormResponse.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = RecruitmentBoardInfoAndFormResponse.class,
                    description = "사용자 최근 임시저장 모집 게시물 조회 성공"),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.USER_NOT_FOUND),
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.BOARD_NOT_FOUND)
            }
    )
    ResponseEntity<ResponseBody<RecruitmentBoardInfoAndFormResponse>> getDraftRecruitmentBoard(
            @Parameter(hidden = true) Long userId);

    @Operation(
            summary = "no-offset 방식 사용자 임시저장 모집 게시물 페이지 조회",
            description = "무한스크롤이 가능한 방식으로 사용자의 임시저장 모집 게시물 목록 페이지를 조회합니다."
    )
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecruitmentBoardNoOffsetResponse.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = RecruitmentBoardNoOffsetResponse.class,
                    description = "사용자 임시저장 모집 게시물 페이지 조회 성공"),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.USER_NOT_FOUND)
            }
    )
    ResponseEntity<ResponseBody<RecruitmentBoardNoOffsetResponse>> getDraftRecruitmentBoardList(
            @Parameter(hidden = true) Long userId,
            @Parameter(description = "페이지 크기", example = "10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "이전 페이지의 가장 마지막 게시물 id(첫 페이지 조회 시 입력 x)", example = "11") @RequestParam(required = false) Long lastBoardId);


    @Operation(
            summary = "페이지 번호 방식 사용자 작성 모집 게시물 페이지 조회",
            description = "페이지 번호를 통해 사용자가 작성한 모집 게시물 목록 페이지를 조회합니다."
    )
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecruitmentBoardPageNumResponse.class)))
    @ApiResponseExplanations(
            success = @ApiSuccessResponseExplanation(
                    responseClass = RecruitmentBoardPageNumResponse.class,
                    description = "사용자 작성 시모집 게시물 페이지 조회 성공"),
            errors = {
                    @ApiErrorResponseExplanation(errorCode = ErrorCode.USER_NOT_FOUND)
            }
    )
    ResponseEntity<ResponseBody<RecruitmentBoardPageNumResponse>> getPublishedUserRecruitmentBoardList(
            @Parameter(hidden = true) Long userId,
            @ParameterObject @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @Parameter(description = "모집 게시물 타입(study, project, mentoring)", example = "study") @RequestParam RecruitmentBoardType recruitmentBoardType);
}
