package com.example.demo.domain.board.service.usecase;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.global.base.dto.page.GlobalPageResponse;
import com.example.demo.domain.board.service.service.LikeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeUseCase {
	private final LikeService likeService;

	public void likeBoard(Long userId, Long boardId) {
		likeService.increaseLike(userId, boardId);
	}

	/**
	 * 좋아요한 게시물 목록 페이징 조회
	 * @param userId
	 * @param pageable
	 * @return BoardPageResponse
	 */
	public GlobalPageResponse getLikes(Long userId, Pageable pageable) {
		return likeService.getLikes(userId, pageable);
	}

	public void unlikeBoard(Long userId, Long boardId) {
		likeService.decreaseLike(userId, boardId);
	}
}
