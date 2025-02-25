package com.example.demo.infra.board.querydsl;

import java.util.Optional;

import com.example.demo.infra.board.entity.Board;

public interface BoardDslRepository {
	Optional<Board> findBoardAndUserAndCategory(Long boardId);

	void increaseViewCount(Long boardId, Integer viewCount);
}
