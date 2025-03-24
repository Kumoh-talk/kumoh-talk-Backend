package com.example.demo.domain.board.service.service;

import com.example.demo.domain.user.entity.UserTarget;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.board.service.entity.BoardFileInfo;
import com.example.demo.domain.board.service.entity.BoardInfo;
import com.example.demo.domain.board.service.implement.BoardReader;
import com.example.demo.domain.board.service.implement.BoardValidator;
import com.example.demo.domain.board.service.implement.FileWriter;
import com.example.demo.domain.user.implement.UserReader;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.global.utils.S3PresignedUrlUtil;
import com.example.demo.global.utils.S3UrlUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardFileService {
	private final BoardReader boardReader;
	private final S3UrlUtil s3UrlUtil;
	private final S3PresignedUrlUtil s3PresignedUrlUtil;
	private final FileWriter fileWriter;
	private final BoardValidator boardValidator;
	private final UserReader userReader;

	@Transactional(readOnly = true)
	public String getPresignedUrl(Long userId, Long boardId, BoardFileInfo boardFileInfo) {
		BoardInfo boardInfo = boardReader.searchSingleBoard(boardId)
			.orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));
		UserTarget userTarget = userReader.findUserTarget(userId)
			.orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
		boardValidator.validateUserEqualBoardUser(userTarget.getUserId(), boardInfo);

		String s3Path = s3UrlUtil.generateBoardS3Path(boardId, boardFileInfo); // TODO : 해당 계층에 대한 의존성 분리에 대해 고민해보기

		return s3PresignedUrlUtil.generatePresignedUrl(s3Path);
	}

	public void saveImageFileUrl(Long userId,Long boardId, BoardFileInfo boardFileInfo) {
		BoardInfo boardInfo = validateUserAndBoardAndFileUrl(userId, boardId,boardFileInfo.getFileUrl());
		fileWriter.saveImageFileUrl(boardFileInfo.getFileUrl(), boardInfo);
	}

	@Transactional(readOnly = true)
	public String getAttachFileUrl(Long boardId) {
		boardValidator.validateExistBoard(boardId);
		return boardReader.readBoardAttachFileUrl(boardId);
	}

	@Transactional
	public void changeAttachFileUrl(Long userId, Long boardId , BoardFileInfo boardFileInfo) {
		BoardInfo boardInfo = validateUserAndBoardAndFileUrl(userId, boardId,boardFileInfo.getFileUrl());
		fileWriter.changeAttachFileUrl(boardFileInfo.getFileUrl(), boardInfo);
	}

	private BoardInfo validateUserAndBoardAndFileUrl(Long userId,Long boardId ,String fileUrl) {
		s3UrlUtil.validateBoardS3Url(fileUrl);
		BoardInfo boardInfo = boardReader.searchSingleBoard(boardId)
			.orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));
		UserTarget userTarget = userReader.findUserTarget(userId)
			.orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
		boardValidator.validateUserEqualBoardUser(userTarget.getUserId(), boardInfo);
		return boardInfo;
	}
}
