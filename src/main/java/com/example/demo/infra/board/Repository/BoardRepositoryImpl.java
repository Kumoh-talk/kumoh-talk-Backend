package com.example.demo.infra.board.Repository;

import org.springframework.stereotype.Repository;

import com.example.demo.domain.board.service.entity.BoardCore;
import com.example.demo.domain.board.service.entity.BoardInfo;
import com.example.demo.domain.board.service.repository.BoardCategoryRepository;
import com.example.demo.domain.board.service.repository.BoardRepository;
import com.example.demo.domain.user.domain.UserTarget;
import com.example.demo.domain.user.repository.UserJpaRepository;
import com.example.demo.infra.board.entity.Board;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepository {
	private final BoardJpaRepository boardJpaRepository;
	private final UserJpaRepository userJpaRepository;

	@Override
	public BoardInfo saveBoard(UserTarget userTarget, BoardCore boardCore) {
		Board board = boardJpaRepository.save(Board.builder()
			.title(boardCore.getTitle())
			.content(boardCore.getContents())
			.boardType(boardCore.getBoardType())
			.status(boardCore.getBoardStatus())
			.user(userJpaRepository.findById(userTarget.getUserId()).get())
			.headImageUrl(boardCore.getBoardHeadImageUrl())
			.build());
		return BoardInfo.builder()
			.boardId(board.getId())
			.boardCore(boardCore)
			.viewCount(board.getViewCount())
			.likeCount(0L)
			.createdAt(board.getCreatedAt())
			.updatedAt(board.getUpdatedAt())
			.userTarget(userTarget)
			.build();
	}
}
