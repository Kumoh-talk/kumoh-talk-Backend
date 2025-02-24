package com.example.demo.domain.board.service.entity;

import com.example.demo.application.board.dto.vo.BoardType;
import com.example.demo.application.board.dto.vo.Status;

import lombok.Getter;

@Getter
public class BoardCore {
	private final String title;

	private final String contents;

	private final BoardType boardType;

	private final String boardHeadImageUrl;

	private Status boardStatus;

	public BoardCore(String title, String contents, BoardType boardType, String boardHeadImageUrl) {
		this.title = title;
		this.contents = contents;
		this.boardType = boardType;
		this.boardHeadImageUrl = boardHeadImageUrl;
		this.boardStatus = Status.DRAFT;
	}

	public BoardCore publishBoard() {
		this.boardStatus = Status.PUBLISHED;
		return this;
	}

	public BoardCore draftBoard() {
		this.boardStatus = Status.DRAFT;
		return this;
	}
}
