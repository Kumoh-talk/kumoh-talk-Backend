package com.example.demo.application.board.controller;

import static com.example.demo.global.base.dto.ResponseUtil.*;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.application.board.api.CategoryApi;
import com.example.demo.domain.base.page.GlobalPageableDto;
import com.example.demo.domain.board.service.entity.BoardTitleInfo;
import com.example.demo.domain.board.service.service.CategoryService;
import com.example.demo.global.base.dto.ResponseBody;
import com.example.demo.global.base.dto.page.GlobalPageResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryController implements CategoryApi {
	private final CategoryService categoryService;

	@GetMapping("/v1/categories")
	public ResponseEntity<ResponseBody<List<String>>> getCategories() {
		return ResponseEntity.ok(createSuccessResponse(categoryService.getCategories()));
	}

	@GetMapping("/v1/categories/boards")
	public ResponseEntity<ResponseBody<GlobalPageResponse<BoardTitleInfo>>> getBoardsByCategoryName(
		@RequestParam("categoryName") String categoryName,
		@PageableDefault(page=0, size=10,sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
		return ResponseEntity.ok(createSuccessResponse(
			GlobalPageResponse.create(
			categoryService.getBoardsByCategoryName(categoryName, GlobalPageableDto.create(pageable)))));
	}
}
