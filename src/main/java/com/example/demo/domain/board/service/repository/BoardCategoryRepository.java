package com.example.demo.domain.board.service.repository;

import com.example.demo.domain.board.service.entity.BoardCategoryNames;
import com.example.demo.domain.board.service.entity.BoardInfo;

public interface BoardCategoryRepository {

	void saveBoardCategories(BoardInfo draftBoard, BoardCategoryNames boardCategoryNames);

	void saveCategoryAndBoardCategory(Long boardId, String categoryName);

	void deleteBoardCategoryByBoardIdAndCategoryName(Long boardId, String categoryName);
}
