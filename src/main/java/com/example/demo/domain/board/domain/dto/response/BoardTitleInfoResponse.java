package com.example.demo.domain.board.domain.dto.response;

import java.time.LocalDateTime;

import com.example.demo.domain.board.domain.dto.vo.Tag;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardTitleInfoResponse {
	private Long boardId;
	private String title;
	private String userName;
	private String boardTag;
	private Long view;
	private Long like;
	private String createdAt;

	public BoardTitleInfoResponse(Long boardId, String title, String userName, Tag tag ,Long view, Long like, LocalDateTime createdAt) {
		this.boardId = boardId;
		this.title = title;
		this.userName = userName;
		this.boardTag = tag.toString();
		this.view = view;
		this.like = like;
		this.createdAt = createdAt.toString();
	}
}
