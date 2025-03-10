package com.example.demo.domain.board.service.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.domain.board.service.entity.BoardTitleInfo;

public interface CategoryRepository {
	List<String> getCategories();

	Page<BoardTitleInfo> findBoardPaegByCategoryName(String categoryName, Pageable pageable);
}
