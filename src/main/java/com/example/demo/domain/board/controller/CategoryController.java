package com.example.demo.domain.board.controller;

import static com.example.demo.global.base.dto.ResponseUtil.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
