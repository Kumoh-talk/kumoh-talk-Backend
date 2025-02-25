package com.example.demo.domain.board.service.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.infra.board.entity.Board;
import com.example.demo.domain.board.service.implement.BoardWriter;
import com.example.demo.domain.board.service.implement.BoardReader;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardAdminUseCase {
	private final BoardWriter boardWriter;
	private final BoardReader boardReader;

	@Transactional
	public Void deleteBoard(Long boardId) {
		Board board = boardReader.validateBoard(boardId);

		boardWriter.removeBoard(board);
		return null;
	}
}
