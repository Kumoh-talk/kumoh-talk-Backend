package com.example.demo.domain.board.api;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.global.base.dto.ResponseBody;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.config.swagger.ApiErrorResponseExplanation;
import com.example.demo.global.config.swagger.ApiResponseExplanations;
import com.example.demo.global.config.swagger.ApiSuccessResponseExplanation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

public interface LikeApi {

	@Operation(
		summary = "좋아요 저장",
		description = "게시글에 좋아요를 저장합니다."
	)
	@ApiResponseExplanations(
		success = @ApiSuccessResponseExplanation(description = "좋아요 저장 성공"),
		errors = {
			@ApiErrorResponseExplanation(errorCode = ErrorCode.BOARD_NOT_FOUND),
			@ApiErrorResponseExplanation(errorCode = ErrorCode.LIKE_USER_NOT_FOUND),
			@ApiErrorResponseExplanation(errorCode = ErrorCode.USER_ALREADY_LIKE_BOARD),
		}
	)
	ResponseEntity<ResponseBody<Void>> saveLike(@Parameter(hidden = true)Long userId, @PathVariable Long boardId);

	@Operation(
		summary = "좋아요 삭제",
		description = "게시글에 저장된 좋아요를 삭제합니다."
	)
	@ApiResponseExplanations(
		success = @ApiSuccessResponseExplanation(description = "좋아요 삭제 성공"),
		errors = {
			@ApiErrorResponseExplanation(errorCode = ErrorCode.BOARD_NOT_FOUND),
			@ApiErrorResponseExplanation(errorCode = ErrorCode.LIKE_USER_NOT_FOUND),
			@ApiErrorResponseExplanation(errorCode = ErrorCode.USER_NOT_LIKE_BOARD),
		}
	)
	ResponseEntity<ResponseBody<Void>> deleteLike(@Parameter(hidden = true)Long userId, @PathVariable Long boardId);

}
