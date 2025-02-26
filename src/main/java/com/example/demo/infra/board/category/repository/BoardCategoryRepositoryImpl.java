package com.example.demo.infra.board.category.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.board.service.entity.BoardCategoryNames;
import com.example.demo.domain.board.service.entity.BoardInfo;
import com.example.demo.domain.board.service.repository.BoardCategoryRepository;
import com.example.demo.infra.board.Repository.BoardJpaRepository;
import com.example.demo.infra.board.entity.Board;
import com.example.demo.infra.board.category.entity.BoardCategory;
import com.example.demo.infra.board.category.entity.Category;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BoardCategoryRepositoryImpl implements BoardCategoryRepository {
	private final BoardCategoryJpaRepository boardCategoryJpaRepository;
	private final BoardJpaRepository boardJpaRepository;
	private final CategoryJpaRepository categoryJpaRepository;

	@Override
	@Transactional
	public void saveBoardCategories(BoardInfo draftBoard, BoardCategoryNames boardCategoryNames) {
		Board board = boardJpaRepository.findById(draftBoard.getBoardId()).get();

		boardCategoryNames.getCategories().forEach(categoryName -> {
			saveCategoryAndBoardCategory(board, categoryName);
		});
	}

	@Override
	public void saveCategoryAndBoardCategory(Long boardId, String categoryName) {
		Board board = boardJpaRepository.findById(boardId).get();
		categoryJpaRepository.findByName(categoryName).ifPresentOrElse(
			category -> {
				BoardCategory boardCategory = new BoardCategory(board, category);
				boardCategoryJpaRepository.save(boardCategory);
			},
			() -> {
				Category category = new Category(categoryName);
				BoardCategory boardCategory = new BoardCategory(board, category);
				boardCategoryJpaRepository.save(boardCategory);
			}
		);
	}

	@Override
	public void deleteBoardCategoryByBoardIdAndCategoryName(Long boardId, String categoryName) {
		BoardCategory boardCategory = boardCategoryJpaRepository
			.findBoardCategoryFetchjoinBoardAndCategory(boardId, categoryName);
		boardCategory.removeAssosiationBoardAndCategory();
		boardCategoryJpaRepository.delete(boardCategory); //TODO : Category 삭제 로직 스케줄링 할 지 고민
	}

	private void saveCategoryAndBoardCategory(Board board, String categoryName) {
		Category category = categoryJpaRepository.findByName(categoryName)
			.orElseGet(() -> categoryJpaRepository.save(new Category(categoryName)));
		BoardCategory boardCategory = new BoardCategory(board, category);
		boardCategoryJpaRepository.save(boardCategory);
	}
}
