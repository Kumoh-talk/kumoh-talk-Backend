package com.example.demo.domain.board.service.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.domain.board.service.entity.BoardTitleInfo;
import com.example.demo.domain.board.service.implement.CategoryReader;
import com.example.demo.global.base.dto.page.GlobalPageResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
	private final CategoryReader categoryReader;

	public List<String> getCategories() {
		return categoryReader.getCategories();
	}

	public GlobalPageResponse<BoardTitleInfo> getBoardsByCategoryName(String categoryName, Pageable pageable) {
		return categoryReader.getBoardPageByCategoryName(categoryName,pageable);
	}
}
