package com.example.demo.infra.board.Repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.demo.domain.board.service.entity.BoardContent;
import com.example.demo.domain.board.service.entity.BoardInfo;
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
	public BoardInfo saveBoard(UserTarget userTarget, BoardContent boardContent) {
		Board board = boardJpaRepository.save(Board.builder()
			.title(boardContent.getTitle())
			.content(boardContent.getContents())
			.boardType(boardContent.getBoardType())
			.status(boardContent.getBoardStatus())
			.user(userJpaRepository.findById(userTarget.getUserId()).get())
			.headImageUrl(boardContent.getBoardHeadImageUrl())
			.build());
		return BoardInfo.builder()
			.boardId(board.getId())
			.boardContent(boardContent)
			.viewCount(board.getViewCount())
			.likeCount(0L)
			.createdAt(board.getCreatedAt())
			.updatedAt(board.getUpdatedAt())
			.userTarget(userTarget)
			.build();
	}

	@Override
	public Optional<BoardInfo> findBoardInfo(Long boardId) {
		return boardJpaRepository
			.findBoardAndUserAndCategory(boardId)
			.map(value -> Board.toBoardInfo(value, boardJpaRepository.countLikesByBoardId(boardId)));
	}

	@Override
	public void countBoardView(Long boardId,Integer viewCount) {
		boardJpaRepository.increaseViewCount(boardId, viewCount);
	}

	@Override
	public void updateBoardContent(Long boardId, BoardContent updateBoardContent) {
		boardJpaRepository.findById(boardId)
			.ifPresent(board -> {
				board.changeBoardInfo(updateBoardContent);
			});
	}
}
