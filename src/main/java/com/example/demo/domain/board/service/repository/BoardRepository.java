package com.example.demo.domain.board.service.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.domain.board.service.entity.vo.BoardType;
import com.example.demo.domain.board.service.entity.BoardContent;
import com.example.demo.domain.board.service.entity.BoardInfo;
import com.example.demo.domain.board.service.entity.BoardTitleInfo;
import com.example.demo.domain.user.domain.UserTarget;

public interface BoardRepository {
	BoardInfo saveBoard(UserTarget userTarget, BoardContent boardContent);

	Optional<BoardInfo> findBoardInfo(Long boardId);

	void countBoardView(Long boardId, Integer viewCount);

	void updateBoardContent(Long boardId, BoardContent updateBoardContent);

	void deleteBoard(Long boardId);

	Page<BoardTitleInfo> findBoardTitleInfoPage(BoardType boardType, Pageable pageableDto);

	Page findDraftBoardByPage(Long userId, Pageable pageableDto);

	Boolean isExistBoard(Long boardId);

	void changeAttachFileUrl(String attachFileUrl, BoardInfo boardInfo);

	String getBoardAttachFileUrl(Long boardId);

	Page findPublishedBoardListByUser(Long userId, BoardType boardType, Pageable pageable);
}
