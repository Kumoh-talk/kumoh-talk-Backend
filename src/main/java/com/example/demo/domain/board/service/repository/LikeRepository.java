package com.example.demo.domain.board.service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.domain.board.service.entity.BoardTitleInfo;

public interface LikeRepository {
	void saveLike(Long userId, Long boardId);

	boolean existsByBoardIdAndUserId(Long boardId, Long userId);

	void deleteLike(Long userId, Long boardId);

	Page<BoardTitleInfo> findLikedBoardPageByUserId(Long userId, Pageable pageable);

	void deleteLikesByBoardId(Long boardId);
}
