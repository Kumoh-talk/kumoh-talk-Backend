package com.example.demo.domain.board.service.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.board.domain.dto.request.FileRequest;
import com.example.demo.domain.board.domain.dto.request.PresignedUrlRequest;
import com.example.demo.domain.board.domain.entity.Board;
import com.example.demo.domain.board.service.service.BoardQueryService;
import com.example.demo.domain.board.service.service.FileUploadService;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.global.utils.S3PresignedUrlUtil;
import com.example.demo.global.utils.S3UrlUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardFileUseCase {
	private final BoardQueryService boardQueryService;
	private final UserService userService;
	private final S3UrlUtil s3UrlUtil;
	private final S3PresignedUrlUtil s3PresignedUrlUtil;
	private final FileUploadService fileUploadService;

	@Transactional(readOnly = true)
	public String getPresignedUrl(Long userId, PresignedUrlRequest presignedUrlRequest) {
		Board board = boardQueryService.validateBoard(presignedUrlRequest.getBoardId());
		User user = userService.validateUser(userId);
		if(!board.getUser().getId().equals(user.getId())) {
			throw new ServiceException(ErrorCode.NOT_ACCESS_USER);
		}
		String s3Path = s3UrlUtil.generateBoardS3Path(presignedUrlRequest);

		return s3PresignedUrlUtil.generatePresignedUrl(s3Path);
	}

	@Transactional
	public void saveImageFileUrl(Long userId, FileRequest fileRequest) {
		Board board = validateUserAndBoardAndFileUrl(userId, fileRequest);
		fileUploadService.saveImageFileUrl(fileRequest.getUrl(), board);
	}

	@Transactional(readOnly = true)
	public String getAttachFileUrl(Long boardId) {
		Board board = boardQueryService.validateBoard(boardId);
		return board.getAttachFileUrl();
	}

	@Transactional
	public void changeAttachFileUrl(Long userId, FileRequest fileRequest) {
		Board board = validateUserAndBoardAndFileUrl(userId, fileRequest);
		fileUploadService.changeAttachFileUrl(fileRequest.getUrl(), board);
	}

	@Transactional
	public void deleteImageFileUrl(Long userId, FileRequest fileRequest) {
		Board board = validateUserAndBoardAndFileUrl(userId, fileRequest);
		fileUploadService.deleteImageFileUrl(board,fileRequest);
	}

	private Board validateUserAndBoardAndFileUrl(Long userId, FileRequest fileRequest) {
		s3UrlUtil.validateBoardS3Url(fileRequest.getUrl());
		Board board = boardQueryService.validateBoard(fileRequest.getBoardId());
		User user = userService.validateUser(userId);
		if(!board.getUser().getId().equals(user.getId())) {
			throw new ServiceException(ErrorCode.NOT_ACCESS_USER);
		}
		return board;
	}
}
