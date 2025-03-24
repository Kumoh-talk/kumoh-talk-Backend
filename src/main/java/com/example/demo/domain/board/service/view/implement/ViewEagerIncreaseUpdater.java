package com.example.demo.domain.board.service.view.implement;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.example.demo.domain.board.service.repository.BoardRepository;

import lombok.RequiredArgsConstructor;


@Service
@Primary
@RequiredArgsConstructor
public class ViewEagerIncreaseUpdater implements ViewCounter {
	private final BoardRepository boardRepository;

	@Override
	public void increaseView(Long boardId) {
		boardRepository.increaseViewCount(boardId);
	}
}
