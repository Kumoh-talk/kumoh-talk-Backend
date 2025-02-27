package com.example.demo.domain.board.service.entity;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class DraftBoardTitle {
	private final Long boardId;
	private final String title;
	private final LocalDateTime createdAt;
	private final LocalDateTime updatedAt;

	public DraftBoardTitle(Long boardId, String title, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.boardId = boardId;
		this.title = title;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
}
