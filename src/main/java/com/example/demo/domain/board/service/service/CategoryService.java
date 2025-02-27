package com.example.demo.domain.board.service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.domain.base.page.GlobalPageableDto;
import com.example.demo.domain.board.service.entity.BoardTitleInfo;
import com.example.demo.domain.board.service.implement.CategoryReader;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
	private final CategoryReader categoryReader;

	public List<String> getCategories() {
		return categoryReader.getCategories();
	}

	public GlobalPageableDto<BoardTitleInfo> getBoardsByCategoryName(String categoryName, GlobalPageableDto pageableDto) {
		return categoryReader.getBoardPageByCategoryName(categoryName,pageableDto);
	}
}
