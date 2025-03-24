package com.example.demo.domain.board.service.entity;

import com.example.demo.domain.board.service.entity.vo.FileType;

import lombok.Getter;

@Getter
public class BoardFileInfo {
	private String fileName;
	private String fileUrl;
	private FileType fileType;

	private BoardFileInfo(String fileName, FileType fileType) {
		this.fileName = fileName;
		this.fileType = fileType;
	}

	private BoardFileInfo(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public static BoardFileInfo of(String fileName, FileType fileType) {
		return new BoardFileInfo(fileName, fileType);
	}

	public static BoardFileInfo of(String fileUrl) {
		return new BoardFileInfo(fileUrl);
	}
}
