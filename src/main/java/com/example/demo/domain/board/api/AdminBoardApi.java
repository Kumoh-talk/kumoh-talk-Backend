package com.example.demo.domain.board.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.global.base.dto.ResponseBody;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.config.swagger.ApiErrorResponseExplanation;
import com.example.demo.global.config.swagger.ApiResponseExplanations;
import com.example.demo.global.config.swagger.ApiSuccessResponseExplanation;

import io.swagger.v3.oas.annotations.Operation;

public interface AdminBoardApi {

	@Operation(summary = "관리자용 세미나/공지사항 게시글 삭제", description = "관리자 계정으로 다른 사용자의 게시글을 삭제합니다.")
	@ApiResponseExplanations(
		success = @ApiSuccessResponseExplanation(
			description = "관리자용 세미나/공지사항 게시글이 삭제됩니다. 다른 사용자의 게시글을 삭제할 수 있습니다."
		),
		errors = {
			@ApiErrorResponseExplanation(errorCode = ErrorCode.BOARD_NOT_FOUND),
		}
	)
	ResponseEntity<ResponseBody<Void>> deleteBoard(@PathVariable Long boardId);
}
