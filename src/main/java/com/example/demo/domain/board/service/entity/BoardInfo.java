package com.example.demo.domain.board.service.entity;

import java.time.LocalDateTime;


import com.example.demo.domain.user.entity.UserTarget;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class BoardInfo {
	private final Long boardId;
	private final BoardContent boardContent;
	private final Long viewCount;
	private final Long likeCount;
	private final LocalDateTime createdAt;
	private final LocalDateTime updatedAt;
	private final UserTarget userTarget;
	private BoardCategoryNames boardCategoryNames;

	@Builder
	private BoardInfo(Long boardId, BoardContent boardContent, Long viewCount, Long likeCount, LocalDateTime createdAt, LocalDateTime updatedAt, UserTarget userTarget,
		BoardCategoryNames boardCategoryNames) {
		this.boardId = boardId;
		this.boardContent = boardContent;
		this.viewCount = viewCount;
		this.likeCount = likeCount;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.userTarget = userTarget;
		this.boardCategoryNames = boardCategoryNames;
	}

	public BoardInfo setBoardCategoryNames(BoardCategoryNames boardCategoryNames) {
		return BoardInfo.builder()
			.boardId(this.boardId)
			.boardContent(this.boardContent)
			.viewCount(this.viewCount)
			.likeCount(this.likeCount)
			.createdAt(this.createdAt)
			.updatedAt(this.updatedAt)
			.userTarget(this.userTarget)
			.boardCategoryNames(boardCategoryNames)
			.build();
	}

	public BoardInfo setBoardContent(BoardContent updateBoardContent) {
		return BoardInfo.builder()
			.boardId(this.boardId)
			.boardContent(updateBoardContent)
			.viewCount(this.viewCount)
			.likeCount(this.likeCount)
			.createdAt(this.createdAt)
			.updatedAt(this.updatedAt)
			.userTarget(this.userTarget)
			.boardCategoryNames(this.boardCategoryNames)
			.build();
	}
}
