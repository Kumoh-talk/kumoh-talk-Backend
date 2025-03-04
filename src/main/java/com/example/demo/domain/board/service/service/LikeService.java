package com.example.demo.domain.board.service.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.domain.board.service.entity.BoardTitleInfo;
import com.example.demo.domain.board.service.implement.BoardValidator;
import com.example.demo.domain.board.service.implement.LikeHandler;
import com.example.demo.domain.user.implement.UserReader;
import com.example.demo.global.base.dto.page.GlobalPageResponse;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeService {
	private final LikeHandler likeHandler;
	private final UserReader userReader;
	private final BoardValidator boardValidator;

	public void likeBoard(Long userId, Long boardId) {
		boardValidator.validateExistBoard(boardId);
		userReader.findUser(userId)
			.orElseThrow(() -> new ServiceException(ErrorCode.LIKE_USER_NOT_FOUND));
		likeHandler.validateExistLike(boardId, userId);
		likeHandler.increaseLike(userId, boardId);
		// likeNotificationHandler.saveLikeNotification(); TODO : LikeNotificationHandler 구현 필요
	}

	public GlobalPageResponse<BoardTitleInfo> getLikes(Long userId, Pageable pageable) {
		return likeHandler.getLikes(userId, pageable);
	}

	public void unlikeBoard(Long userId, Long boardId) {
		boardValidator.validateExistBoard(boardId);
		userReader.findUser(userId)
			.orElseThrow(() -> new ServiceException(ErrorCode.LIKE_USER_NOT_FOUND));
		likeHandler.validateNonExistLike(boardId, userId);

		likeHandler.decreaseLike(userId, boardId);
	}
}
