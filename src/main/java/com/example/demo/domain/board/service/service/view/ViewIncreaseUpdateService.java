package com.example.demo.domain.board.service.service.view;


import org.springframework.stereotype.Service;

import com.example.demo.infra.board.Repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ViewIncreaseUpdateService implements ViewIncreaseService {
	private final BoardRepository boardRepository;

	@Override
	public void increaseView(Long boardId) {
		boardRepository.increaseViewCount(boardId);
	}

}
