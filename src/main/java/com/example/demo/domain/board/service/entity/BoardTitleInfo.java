package com.example.demo.domain.board.service.entity;

import java.time.LocalDateTime;

import com.example.demo.domain.board.service.entity.vo.BoardType;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardTitleInfo {
	private Long boardId;
	private String title;
	private String userName;
	private String boardTag;
	private Long view;
	private Long like;
	private String headImageUrl;
	private String createdAt;

	public BoardTitleInfo(Long boardId, String title, String userName, BoardType boardType,Long view, Long like,String headImageUrl ,LocalDateTime createdAt) {
		this.boardId = boardId;
		this.title = title;
		this.userName = userName;
		this.boardTag = boardType.toString();
		this.view = view;
		this.like = like;
		this.headImageUrl = headImageUrl;
		this.createdAt = createdAt.toString();
	}
}
