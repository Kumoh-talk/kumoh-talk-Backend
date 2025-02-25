package com.example.demo.domain.board.service.entity;

import com.example.demo.application.board.dto.vo.BoardType;
import com.example.demo.application.board.dto.vo.Status;

import lombok.Getter;

@Getter
public class BoardContent {
	private final String title;

	private final String contents;

	private final BoardType boardType;

	private final String boardHeadImageUrl;

	private Status boardStatus;

	public BoardContent(String title, String contents, BoardType boardType, String boardHeadImageUrl, Status boardStatus) {
		this.title = title;
		this.contents = contents;
		this.boardType = boardType;
		this.boardHeadImageUrl = boardHeadImageUrl;
		this.boardStatus = boardStatus;
	}

	public BoardContent publishBoard() {
		this.boardStatus = Status.PUBLISHED;
		return this;
	}

	public BoardContent draftBoard() {
		this.boardStatus = Status.DRAFT;
		return this;
	}
}
