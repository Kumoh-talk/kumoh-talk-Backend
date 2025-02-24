package com.example.demo.domain.board.service.implement;

import org.springframework.stereotype.Component;

import com.example.demo.domain.board.service.entity.BoardCategoryNames;
import com.example.demo.domain.board.service.entity.BoardInfo;
import com.example.demo.domain.board.service.repository.BoardCategoryRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BoardCategoryWriter {
	private final BoardCategoryRepository boardCategoryRepository;

	public void saveCategoryNames(BoardInfo draftBoard, BoardCategoryNames boardCategoryNames) {
		boardCategoryRepository.saveBoardCategories(draftBoard, boardCategoryNames);
	}
}
