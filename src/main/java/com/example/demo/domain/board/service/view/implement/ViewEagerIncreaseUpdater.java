package com.example.demo.domain.board.service.view.implement;


import org.springframework.stereotype.Service;

import com.example.demo.infra.board.Repository.BoardJpaRepository;

import lombok.RequiredArgsConstructor;

/**
 * ViewEagerIncreaseUpdater
 * 해당 클래스는 현재 사용되지 않습니다.
 * 현재 사용되는 클래스는 ViewBulkUpdater 입니다.
 */
@Deprecated
@Service
@RequiredArgsConstructor
public class ViewEagerIncreaseUpdater implements ViewCounter {
	private final BoardJpaRepository boardJpaRepository;

	@Override
	public void increaseView(Long boardId) {
		boardJpaRepository.increaseViewCount(boardId);
	}

}
