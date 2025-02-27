package com.example.demo.domain.board.service.implement;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.base.page.GlobalPageableDto;
import com.example.demo.domain.board.service.entity.BoardTitleInfo;
import com.example.demo.domain.board.service.repository.CategoryRepository;

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
	public GlobalPageableDto<BoardTitleInfo> getBoardPageByCategoryName(String categoryName, GlobalPageableDto pageableDto) {
		return categoryRepository.findBoardPaegByCategoryName(categoryName, pageableDto);
	}
}
