package com.example.demo.domain.board.service.repository;

import java.util.List;

import com.example.demo.domain.base.page.GlobalPageableDto;
import com.example.demo.domain.board.service.entity.BoardTitleInfo;

public interface CategoryRepository {
	List<String> getCategories();

	GlobalPageableDto<BoardTitleInfo> findBoardPaegByCategoryName(String categoryName, GlobalPageableDto pageableDto);
}
