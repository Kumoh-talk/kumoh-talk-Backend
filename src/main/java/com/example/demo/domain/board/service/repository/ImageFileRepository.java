package com.example.demo.domain.board.service.repository;

import com.example.demo.domain.board.service.entity.BoardInfo;

public interface ImageFileRepository {
	void saveImageFile(String url, BoardInfo boardInfo);
}
