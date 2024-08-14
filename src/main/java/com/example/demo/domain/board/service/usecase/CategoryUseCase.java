package com.example.demo.domain.board.service.usecase;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.domain.board.domain.dto.response.BoardPageResponse;
import com.example.demo.domain.board.service.service.CategoryQueryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryUseCase {
	private final CategoryQueryService categoryQueryService;

	/**
	 * 카테고리 목록 조회 (TODO : 현재는 전체 카테고리 이름을 가지고 오는데 프론트에서 페이징이나 필터링이 필요하면 추가)
	 * @return 카테고리 이름 List
	 */
	public List<String> getCategories() {
		return categoryQueryService.getCategories();
	}

	public BoardPageResponse getBoardsByCategoryName(String categoryName, Pageable pageable) {
		return categoryQueryService.getBoardPageByCategoryName(categoryName,pageable);

	}
}
