package com.example.demo.domain.board.service.service;

import org.springframework.stereotype.Service;

import com.example.demo.domain.base.page.GlobalPageableDto;
import com.example.demo.domain.board.service.entity.BoardTitleInfo;
import com.example.demo.domain.board.service.implement.BoardValidator;
import com.example.demo.domain.board.service.implement.LikeHandler;
import com.example.demo.domain.user.implement.UserReader;
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

	public GlobalPageableDto<BoardTitleInfo> getLikes(Long userId, GlobalPageableDto pageableDto) {
		return likeHandler.getLikes(userId, pageableDto);
	}

	public void unlikeBoard(Long userId, Long boardId) {
		boardValidator.validateExistBoard(boardId);
		userReader.findUser(userId)
			.orElseThrow(() -> new ServiceException(ErrorCode.LIKE_USER_NOT_FOUND));
		likeHandler.validateNonExistLike(boardId, userId);

		likeHandler.decreaseLike(userId, boardId);
	}
}
