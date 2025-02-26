package com.example.demo.infra.board.Repository;

import com.example.demo.domain.board.service.entity.BoardCategoryNames;
import com.example.demo.domain.board.service.entity.BoardInfo;
import com.example.demo.domain.board.service.repository.BoardCategoryRepository;
import com.example.demo.infra.board.entity.Board;
import com.example.demo.infra.board.entity.BoardCategory;
import com.example.demo.infra.board.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BoardCategoryRepositoryImpl implements BoardCategoryRepository {
	private final BoardCategoryJpaRepository boardCategoryJpaRepository;
	private final BoardJpaRepository boardJpaRepository;
	private final CategoryJpaRepository categoryJpaRepository;

	@Override
	public void saveBoardCategories(BoardInfo draftBoard, BoardCategoryNames boardCategoryNames) {
		Board board = boardJpaRepository.findById(draftBoard.getBoardId()).get();

		boardCategoryNames.getCategories().forEach(categoryName -> {
			saveCategoryAndBoardCategory(board, categoryName);
		});
	}
	private void saveCategoryAndBoardCategory(Board board, String categoryName) {
		Category category = categoryJpaRepository.findByName(categoryName)
			.orElseGet(() -> categoryJpaRepository.save(new Category(categoryName)));
		BoardCategory boardCategory = new BoardCategory(board, category);
		boardCategoryJpaRepository.save(boardCategory);
	}
}
