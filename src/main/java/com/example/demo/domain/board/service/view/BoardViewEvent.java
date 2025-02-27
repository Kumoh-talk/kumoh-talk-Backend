package com.example.demo.domain.board.service.view;

import org.springframework.context.ApplicationEvent;

public class BoardViewEvent extends ApplicationEvent {
	private final Long boardId;

	public BoardViewEvent(Object source, Long boardId) {
		super(source);
		this.boardId = boardId;
	}

	public Long getBoardId() {
		return boardId;
	}
}
