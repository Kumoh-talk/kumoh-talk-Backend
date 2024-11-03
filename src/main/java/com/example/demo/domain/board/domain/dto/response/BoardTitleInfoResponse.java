package com.example.demo.domain.board.domain.dto.response;

import java.time.LocalDateTime;

import com.example.demo.domain.board.domain.dto.vo.BoardType;

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
	private String headImageUrl;
	private String createdAt;

	public BoardTitleInfoResponse(Long boardId, String title, String userName, BoardType boardType,Long view, Long like,String headImageUrl ,LocalDateTime createdAt) {
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
