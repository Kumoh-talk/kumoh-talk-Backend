package com.example.demo.infra.board.repository;

import org.springframework.stereotype.Repository;

import com.example.demo.domain.board.service.entity.BoardInfo;
import com.example.demo.domain.board.service.repository.ImageFileRepository;
import com.example.demo.infra.board.entity.Board;
import com.example.demo.infra.board.entity.ImageFile;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ImageFileRepositoryImpl implements ImageFileRepository {
	private final ImageFileJpaRepository imageFileJpaRepository;

	@Override
	public void saveImageFile(String url, BoardInfo boardInfo) {
		ImageFile imageFile = ImageFile.of(url, new Board(boardInfo.getBoardId()));
		imageFileJpaRepository.save(imageFile);
	}
}
