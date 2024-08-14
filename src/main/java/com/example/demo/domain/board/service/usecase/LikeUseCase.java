package com.example.demo.domain.board.service.usecase;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.board.domain.dto.response.BoardPageResponse;
import com.example.demo.domain.board.service.service.LikeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeUseCase {
	private final LikeService likeService;

	/**
	 * 게시물 좋아요
	 * @param userId
	 * @param boardId
	 */
	public void likeBoard(Long userId, Long boardId) {
		likeService.increaseLike(userId, boardId);
	}

	public BoardPageResponse getLikes(Long userId, Pageable pageable) {
		return likeService.getLikes(userId, pageable);
	}
}
