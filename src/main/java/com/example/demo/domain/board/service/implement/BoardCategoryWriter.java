package com.example.demo.domain.board.service.implement;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

	@Transactional
	public BoardInfo modifyBoardCategories(BoardInfo savedBoardInfo,
		BoardCategoryNames updateBoardCategoryNames) {
		Long boardId = savedBoardInfo.getBoardId();

		Set<String> existing = new HashSet<>(savedBoardInfo.getBoardCategoryNames().getCategories());
		Set<String> updated = new HashSet<>(updateBoardCategoryNames.getCategories());

		Set<String> toAdd = getCategoriesToAdd(existing, updated);
		Set<String> toRemove = getCategoriesToRemove(existing, updated);

		toAdd.forEach(categoryName -> boardCategoryRepository.saveCategoryAndBoardCategory(boardId, categoryName));
		toRemove.forEach(categoryName -> boardCategoryRepository.deleteBoardCategoryByBoardIdAndCategoryName(boardId, categoryName));

		return savedBoardInfo.setBoardCategoryNames(updateBoardCategoryNames);
	}

	// 업데이트된 카테고리 중, 기존에 없는 카테고리 목록
	private Set<String> getCategoriesToAdd(Set<String> existing, Set<String> updated) {
		return updated.stream()
			.filter(name -> !existing.contains(name))
			.collect(Collectors.toSet());
	}

	// 기존 카테고리 중, 업데이트에 포함되지 않은 카테고리 목록
	private Set<String> getCategoriesToRemove(Set<String> existing, Set<String> updated) {
		return existing.stream()
			.filter(name -> !updated.contains(name))
			.collect(Collectors.toSet());
	}
}
