package com.example.demo.domain.board.service.implement;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.board.service.entity.BoardTitleInfo;
import com.example.demo.domain.board.service.repository.CategoryRepository;
import com.example.demo.global.base.dto.page.GlobalPageResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryReader {
	private final CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	public List<String> getCategories() {
		return categoryRepository.getCategories();

	}

	@Transactional(readOnly = true)
	public GlobalPageResponse<BoardTitleInfo> getBoardPageByCategoryName(String categoryName, Pageable pageable) {
		return GlobalPageResponse.create(categoryRepository.findBoardPaegByCategoryName(categoryName, pageable));
	}
}
