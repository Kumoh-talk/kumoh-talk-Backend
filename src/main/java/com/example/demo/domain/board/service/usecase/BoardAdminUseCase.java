package com.example.demo.domain.board.service.usecase;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.board.domain.entity.Board;
import com.example.demo.domain.board.service.service.BoardCommandService;
import com.example.demo.domain.board.service.service.BoardQueryService;
import com.example.demo.global.base.dto.ResponseBody;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardAdminUseCase {
	private final BoardCommandService boardCommandService;
	private final BoardQueryService boardQueryService;

	@Transactional
	public Void deleteBoard(Long boardId) {
		Board board = boardQueryService.validateBoard(boardId);

		boardCommandService.removeBoard(board);
		return null;
	}
}
