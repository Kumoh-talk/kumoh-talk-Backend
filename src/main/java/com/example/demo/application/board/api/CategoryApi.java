package com.example.demo.application.board.api;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.application.board.dto.response.BoardTitleInfoResponse;
import com.example.demo.global.base.dto.ResponseBody;
import com.example.demo.global.base.dto.page.GlobalPageResponse;
import com.example.demo.global.config.swagger.ApiResponseExplanations;
import com.example.demo.global.config.swagger.ApiSuccessResponseExplanation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface CategoryApi {

	@Operation(
		summary = "카테고리 목록 조회",
		description = "게시판 카테고리 목록을 조회합니다."
	)
	@ApiResponseExplanations(
		success = @ApiSuccessResponseExplanation(
			responseClass = List.class,
			description = "카테고리 목록 조회 성공, 카테고리 목록을 반환합니다."
		)
	)
	ResponseEntity<ResponseBody<List<String>>> getCategories();


	@Operation(
		summary = "카테고리별 게시판 목록 조회",
		description = "특정 카테고리에 속한 게시판 목록을 조회합니다."
	)
	@ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalPageResponse.class))
	)
	@ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = BoardTitleInfoResponse.class))
	)
	@ApiResponseExplanations(
		success = @ApiSuccessResponseExplanation(
			responseClass = GlobalPageResponse.class,
			description = "카테고리별 게시판 목록 조회 성공, 카테고리별 게시판 목록을 반환합니다."
		)
	)
	ResponseEntity<ResponseBody<GlobalPageResponse<BoardTitleInfoResponse>>> getBoardsByCategoryName(
		@Schema(example = "카테고리 네임") @RequestParam("categoryName") String categoryName,
		@ParameterObject
		@PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable);
}
