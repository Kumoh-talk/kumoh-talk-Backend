package com.example.demo.domain.board.service.repository;

import com.example.demo.domain.base.page.GlobalPageableDto;
import com.example.demo.domain.board.service.entity.BoardTitleInfo;

public interface LikeRepository {
	void saveLike(Long userId, Long boardId);

	boolean existsByBoardIdAndUserId(Long boardId, Long userId);

	void deleteLike(Long userId, Long boardId);

	GlobalPageableDto<BoardTitleInfo> findLikedBoardPageByUserId(Long userId, GlobalPageableDto pageableDto);
}
