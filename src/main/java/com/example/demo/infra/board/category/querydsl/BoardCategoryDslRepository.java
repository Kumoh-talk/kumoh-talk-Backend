package com.example.demo.infra.board.category.querydsl;

import com.example.demo.infra.board.category.entity.BoardCategory;

public interface BoardCategoryDslRepository {
	BoardCategory findBoardCategoryFetchjoinBoardAndCategory(Long boardId, String categoryName);

	void deleteBoardCategoriesByBoardId(Long boardId);
}
