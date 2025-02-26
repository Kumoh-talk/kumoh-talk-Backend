package com.example.demo.infra.board.querydsl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.application.board.dto.vo.BoardType;
import com.example.demo.domain.board.service.entity.BoardTitleInfo;
import com.example.demo.infra.board.entity.Board;

public interface BoardDslRepository {
	Optional<Board> findBoardAndUserAndCategory(Long boardId);

	void increaseViewCount(Long boardId, Integer viewCount);

	Page<BoardTitleInfo> findBoardByPage(BoardType boardType, Pageable pageable);
}
