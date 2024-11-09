package com.example.demo.domain.board.api;

import java.util.List;


import org.springframework.http.ResponseEntity;

import com.example.demo.global.base.dto.ResponseBody;
import com.example.demo.global.config.swagger.ApiResponseExplanations;
import com.example.demo.global.config.swagger.ApiSuccessResponseExplanation;

import io.swagger.v3.oas.annotations.Operation;

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
}
