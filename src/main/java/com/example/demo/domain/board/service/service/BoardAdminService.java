package com.example.demo.domain.board.service.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.board.service.implement.BoardCategoryWriter;
import com.example.demo.domain.board.service.implement.BoardValidator;
import com.example.demo.domain.board.service.implement.BoardWriter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardAdminService {
	private final BoardWriter boardWriter;
	private final BoardValidator boardValidator;
	private final BoardCategoryWriter boardCategoryWriter;

	@Transactional
	public Void deleteBoard(Long boardId) {
		boardValidator.validateExistBoard(boardId);

		boardCategoryWriter.removeBoardCategoriesByBoardId(boardId);
		boardWriter.removeBoardByBoardId(boardId);
		return null;
	}
}
