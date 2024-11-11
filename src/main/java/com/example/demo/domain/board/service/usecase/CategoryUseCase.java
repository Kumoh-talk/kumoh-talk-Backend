package com.example.demo.domain.board.service.usecase;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.domain.board.domain.dto.response.BoardTitleInfoResponse;
import com.example.demo.global.base.dto.page.GlobalPageResponse;
import com.example.demo.domain.board.service.service.CategoryQueryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryUseCase {
	private final CategoryQueryService categoryQueryService;

	public List<String> getCategories() {
		return categoryQueryService.getCategories();
	}

	public GlobalPageResponse<BoardTitleInfoResponse> getBoardsByCategoryName(String categoryName, Pageable pageable) {
		return categoryQueryService.getBoardPageByCategoryName(categoryName,pageable);
	}
}
