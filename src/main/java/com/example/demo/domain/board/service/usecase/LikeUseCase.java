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

	/**
	 * 좋아요한 게시물 목록 페이징 조회
	 * @param userId
	 * @param pageable
	 * @return BoardPageResponse
	 */
	public BoardPageResponse getLikes(Long userId, Pageable pageable) {
		return likeService.getLikes(userId, pageable);
	}

	/**
	 * 게시물 좋아요 취소
	 * @param userId
	 * @param boardId
	 */
	public void unlikeBoard(Long userId, Long boardId) {
		likeService.decreaseLike(userId, boardId);
	}
}
