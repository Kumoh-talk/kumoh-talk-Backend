package com.example.demo.domain.board.service.implement;

import org.springframework.stereotype.Service;

import com.example.demo.domain.board.service.entity.BoardInfo;
import com.example.demo.domain.board.service.repository.BoardRepository;
import com.example.demo.domain.board.service.repository.ImageFileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileWriter {
	private final ImageFileRepository imageFileRepository;
	private final BoardRepository boardRepository;

	public void changeAttachFileUrl(String attachFileUrl, BoardInfo boardInfo) {
		boardRepository.changeAttachFileUrl(attachFileUrl, boardInfo);
	}

	public void saveImageFileUrl(String url, BoardInfo boardInfo) {
		imageFileRepository.saveImageFile(url, boardInfo);

	}
}
