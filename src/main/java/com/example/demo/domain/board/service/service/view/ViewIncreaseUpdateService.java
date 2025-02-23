package com.example.demo.domain.board.service.service.view;


import org.springframework.stereotype.Service;

import com.example.demo.infra.board.Repository.BoardJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ViewIncreaseUpdateService implements ViewIncreaseService {
	private final BoardJpaRepository boardJpaRepository;

	@Override
	public void increaseView(Long boardId) {
		boardJpaRepository.increaseViewCount(boardId);
	}

}
