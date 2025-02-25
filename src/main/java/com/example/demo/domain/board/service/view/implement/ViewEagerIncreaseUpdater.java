package com.example.demo.domain.board.service.view.implement;


import org.springframework.stereotype.Service;

import com.example.demo.infra.board.Repository.BoardJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ViewEagerIncreaseUpdater implements ViewCounter {
	private final BoardJpaRepository boardJpaRepository;

	@Override
	public void increaseView(Long boardId) {
		boardJpaRepository.increaseViewCount(boardId);
	}

}
