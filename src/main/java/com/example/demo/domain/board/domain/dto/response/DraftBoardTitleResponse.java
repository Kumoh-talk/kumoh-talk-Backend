package com.example.demo.domain.board.domain.dto.response;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class DraftBoardTitleResponse {
	private final Long boardId;
	private final String title;
	private final LocalDateTime createdAt;
	private final LocalDateTime updatedAt;

	public DraftBoardTitleResponse(Long boardId, String title, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.boardId = boardId;
		this.title = title;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
}
