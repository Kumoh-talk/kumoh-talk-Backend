package com.example.demo.domain.board.service.usecase;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.domain.board.service.entity.BoardTitleInfo;
import com.example.demo.global.base.dto.page.GlobalPageResponse;
import com.example.demo.domain.board.service.implement.LikeHandler;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeService {
	private final LikeHandler likeHandler;

	public void likeBoard(Long userId, Long boardId) {
		likeHandler.increaseLike(userId, boardId);
	}

	/**
	 * 좋아요한 게시물 목록 페이징 조회
	 * @param userId
	 * @param pageable
	 * @return BoardPageResponse
	 */
	public GlobalPageResponse<BoardTitleInfo> getLikes(Long userId, Pageable pageable) {
		return likeHandler.getLikes(userId, pageable);
	}

	public void unlikeBoard(Long userId, Long boardId) {
		likeHandler.decreaseLike(userId, boardId);
	}
}
