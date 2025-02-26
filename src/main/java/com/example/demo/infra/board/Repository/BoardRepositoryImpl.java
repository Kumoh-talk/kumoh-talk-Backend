package com.example.demo.infra.board.Repository;

import com.example.demo.domain.board.service.entity.BoardCore;
import com.example.demo.domain.board.service.entity.BoardInfo;
import com.example.demo.domain.board.service.repository.BoardRepository;
import com.example.demo.domain.user.entity.UserTarget;
import com.example.demo.infra.board.entity.Board;
import com.example.demo.infra.user.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
