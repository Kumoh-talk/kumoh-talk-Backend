package com.example.demo.application.board.api;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.application.board.dto.request.FileRequest;
import com.example.demo.application.board.dto.request.PresignedUrlRequest;
import com.example.demo.global.base.dto.ResponseBody;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.config.swagger.ApiErrorResponseExplanation;
import com.example.demo.global.config.swagger.ApiResponseExplanations;
import com.example.demo.global.config.swagger.ApiSuccessResponseExplanation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;

public interface BoardFileApi {

	@Operation(
		summary = "이미지 업로드를 위한 Presigned URL 생성",
		description = "이미지 업로드를 위한 Presigned URL을 생성합니다. 이미지 업로드를 위한 URL을 생성하고, 이 URL을 통해 이미지를 업로드합니다."
	)
	@ApiResponseExplanations(
		success = @ApiSuccessResponseExplanation(
			responseClass = String.class,
			description = "Presigned URL 생성 성공, 이미지 업로드를 위한 URL을 반환합니다."
		),
		errors = {
			@ApiErrorResponseExplanation(errorCode = ErrorCode.BOARD_NOT_FOUND),
			@ApiErrorResponseExplanation(errorCode = ErrorCode.USER_NOT_FOUND),
			@ApiErrorResponseExplanation(errorCode = ErrorCode.NOT_ACCESS_USER),
		}
	)
	ResponseEntity<ResponseBody<String>> getPresignedUrl(@Parameter(hidden = true) Long userId,
		@RequestBody @Valid PresignedUrlRequest presignedUrlRequest);

	@Operation(
		summary = "이미지 파일 URL 저장",
		description = "이미지 파일 URL을 저장합니다. 이미지 파일 URL을 저장하면, 해당 URL을 통해 이미지를 조회할 수 있습니다."
	)
	@ApiResponseExplanations(
		success = @ApiSuccessResponseExplanation(
			description = "이미지 파일 URL 저장 성공"
		),
		errors = {
			@ApiErrorResponseExplanation(errorCode = ErrorCode.FILE_URL_NOT_MATCHED),
			@ApiErrorResponseExplanation(errorCode = ErrorCode.BOARD_NOT_FOUND),
			@ApiErrorResponseExplanation(errorCode = ErrorCode.USER_NOT_FOUND),
			@ApiErrorResponseExplanation(errorCode = ErrorCode.NOT_ACCESS_USER),
		}
	)
	ResponseEntity<ResponseBody<Void>> saveImageFileUrl(@Parameter(hidden = true)Long userId,
		@RequestBody @Valid FileRequest fileRequest);

	@Operation(
		summary = "첨부 파일 URL 변경",
		description = "첨부 파일 URL을 변경합니다. 첨부 파일 URL을 변경하면, 해당 URL을 통해 첨부 파일을 조회할 수 있습니다."
	)
	@ApiResponseExplanations(
		success = @ApiSuccessResponseExplanation(
			description = "첨부 파일 URL 변경 성공"
		),
		errors = {
			@ApiErrorResponseExplanation(errorCode = ErrorCode.FILE_URL_NOT_MATCHED),
			@ApiErrorResponseExplanation(errorCode = ErrorCode.BOARD_NOT_FOUND),
			@ApiErrorResponseExplanation(errorCode = ErrorCode.USER_NOT_FOUND),
			@ApiErrorResponseExplanation(errorCode = ErrorCode.NOT_ACCESS_USER),
		}
	)
	ResponseEntity<ResponseBody<Void>> changeAttachFileUrl(@Parameter(hidden = true)Long userId, @RequestBody @Valid FileRequest fileRequest);

	@Operation(
		summary = "첨부 파일 URL 조회",
		description = "첨부 파일 URL을 조회합니다. 첨부 파일 URL을 조회하면, 해당 URL을 통해 첨부 파일을 조회할 수 있습니다."
	)
	@ApiResponseExplanations(
		success = @ApiSuccessResponseExplanation(
			responseClass = String.class,
			description = "첨부 파일 URL 조회 성공, 첨부 파일 URL을 반환합니다."
		),
		errors = {
			@ApiErrorResponseExplanation(errorCode = ErrorCode.BOARD_NOT_FOUND),
		}
	)
	ResponseEntity<ResponseBody<String>> getAttachFileUrl(@PathVariable Long boardId);

	@Operation(
		summary = "이미지 파일 URL 삭제",
		description = "이미지 파일 URL을 삭��합니다. 이미지 파일 URL을 삭제하면, 해당 URL을 통해 이미지를 조회할 수 없습니다."
	)
	@ApiResponseExplanations(
		success = @ApiSuccessResponseExplanation(
			description = "이미지 파일 URL 삭제 성공"
		),
		errors = {
			@ApiErrorResponseExplanation(errorCode = ErrorCode.FILE_URL_NOT_MATCHED),
			@ApiErrorResponseExplanation(errorCode = ErrorCode.BOARD_NOT_FOUND),
			@ApiErrorResponseExplanation(errorCode = ErrorCode.USER_NOT_FOUND),
			@ApiErrorResponseExplanation(errorCode = ErrorCode.NOT_ACCESS_USER),
		}
	)
	ResponseEntity<ResponseBody<Void>> deleteImageFileUrl(@Parameter(hidden = true)Long userId, @RequestBody @Valid FileRequest fileRequest);
}
