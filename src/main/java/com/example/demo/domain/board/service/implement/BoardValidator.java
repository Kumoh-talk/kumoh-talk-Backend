package com.example.demo.domain.board.service.implement;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.demo.application.board.dto.vo.Status;
import com.example.demo.domain.board.service.entity.BoardInfo;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.global.jwt.JwtAuthentication;
import com.example.demo.infra.board.entity.Board;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BoardValidator {

	// 임시저장 게시물은 작성자만 조회 가능하기 때문에 작성자인지 확인
	public void validateDraftBoardIsOwner(Long authenticatedUserId, BoardInfo boardInfo) {
		if(isDraftBoard(boardInfo)) {
			if (isAuthenticated(authenticatedUserId)) {
				if(!boardInfo.getUserTarget().getUserId().equals(authenticatedUserId)) {
					throw new ServiceException(ErrorCode.DRAFT_NOT_ACCESS_USER);
				}
			} else {
				throw new ServiceException(ErrorCode.NOT_ACCESS_USER);
			}
		}
	}

	private static boolean isAuthenticated(Long authenticatedUserId) {
		return authenticatedUserId != null;
	}

	private static boolean isDraftBoard(BoardInfo boardInfo) {
		return boardInfo.getBoardContent().getBoardStatus().equals(Status.DRAFT);
	}
}
