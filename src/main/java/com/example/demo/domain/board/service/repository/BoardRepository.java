package com.example.demo.domain.board.service.repository;

import java.util.Optional;

import com.example.demo.domain.board.service.entity.BoardContent;
import com.example.demo.domain.board.service.entity.BoardInfo;
import com.example.demo.domain.user.domain.UserTarget;

public interface BoardRepository {
	BoardInfo saveBoard(UserTarget userTarget, BoardContent boardContent);

	Optional<BoardInfo> findBoardInfo(Long boardId);

	void countBoardView(Long boardId, Integer viewCount);

	void updateBoardContent(Long boardId, BoardContent updateBoardContent);

	void deleteBoard(Long boardId);
}
