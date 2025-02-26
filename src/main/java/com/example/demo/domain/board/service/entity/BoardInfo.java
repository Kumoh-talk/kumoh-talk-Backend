package com.example.demo.domain.board.service.entity;

import com.example.demo.domain.user.entity.UserTarget;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardInfo {
	private final Long boardId;
	private final BoardCore boardCore;
	private final Long viewCount;
	private final Long LikeCount;
	private final LocalDateTime createdAt;
	private final LocalDateTime updatedAt;
	private final UserTarget userTarget;
	private BoardCategoryNames boardCategoryNames;

	@Builder
	private BoardInfo(Long boardId, BoardCore boardCore, Long viewCount, Long likeCount, LocalDateTime createdAt, LocalDateTime updatedAt, UserTarget userTarget,
		BoardCategoryNames boardCategoryNames) {
		this.boardId = boardId;
		this.boardCore = boardCore;
		this.viewCount = viewCount;
		this.LikeCount = likeCount;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.userTarget = userTarget;
		this.boardCategoryNames = boardCategoryNames;
	}

	public void setBoardCategoryNames(BoardCategoryNames boardCategoryNames) {
		this.boardCategoryNames = boardCategoryNames;
	}
}
