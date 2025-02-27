package com.example.demo.domain.board.service.repository;

import java.util.Optional;

import com.example.demo.domain.board.service.entity.BoardInfo;
import com.example.demo.infra.board.entity.ImageFile;

public interface ImageFileRepository {
	void saveImageFile(String url, BoardInfo boardInfo);
}
