package com.example.demo.domain.board.controller;

import static com.example.demo.global.base.dto.ResponseUtil.*;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.board.domain.dto.response.BoardPageResponse;
import com.example.demo.domain.board.service.usecase.CategoryUseCase;
import com.example.demo.global.base.dto.ResponseBody;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryController {
	private final CategoryUseCase categoryUseCase;

	@GetMapping("/v1/categories")
	public ResponseEntity<ResponseBody<List<String>>> getCategories() {
		return ResponseEntity.ok(createSuccessResponse(categoryUseCase.getCategories()));
	}

	@GetMapping("/v1/categories/boards")
	public ResponseEntity<ResponseBody<BoardPageResponse>> getBoardsByCategoryName(
		@RequestParam("categoryName") String categoryName,
		@RequestParam(defaultValue = "1") int pageNum,
		@RequestParam(defaultValue = "10") int pageSize,
		@RequestParam(defaultValue = "DESC") String sortDirection,
		@RequestParam(defaultValue = "createdAt") String sortBy){
		Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by(Sort.Direction.fromString(sortDirection), sortBy));
		return ResponseEntity.ok(createSuccessResponse(categoryUseCase.getBoardsByCategoryName(categoryName,pageable)));
	}
}
