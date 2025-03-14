package com.example.demo.infra.board.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.board.service.entity.vo.BoardType;
import com.example.demo.domain.board.service.entity.BoardContent;
import com.example.demo.domain.board.service.entity.BoardInfo;
import com.example.demo.domain.board.service.entity.BoardTitleInfo;
import com.example.demo.domain.board.service.entity.DraftBoardTitle;
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

	@Override
	public void deleteBoard(Long boardId) {
		boardJpaRepository.deleteById(boardId);
	}

	@Override
	public Page<BoardTitleInfo> findBoardTitleInfoPage(BoardType boardType, Pageable pageable) {
		return boardJpaRepository.findBoardByPage(boardType,pageable);
	}

	@Override
	public Page<DraftBoardTitle> findDraftBoardByPage(Long userId, Pageable pageable) {
		return boardJpaRepository.findDraftBoardByPage(userId, pageable);
	}

	@Override
	public Boolean isExistBoard(Long boardId) {
		return boardJpaRepository.existsById(boardId); // TODO : exist 가 어떻게 동작하는지 확인해야함
	}

	@Override
	public void changeAttachFileUrl(String attachFileUrl, BoardInfo boardInfo) {
		boardJpaRepository.findById(boardInfo.getBoardId())
			.ifPresent(board -> {
				board.changeAttachFileUrl(attachFileUrl);
			});
	}

	@Override
	public String getBoardAttachFileUrl(Long boardId) {
		return boardJpaRepository.getAttachFileUrl(boardId);
	}

	@Override
	public Page findPublishedBoardListByUser(Long userId, BoardType boardType, Pageable pageable) {
		return boardJpaRepository.findPublishedBoardListByUser(userId, boardType, pageable);
	}

	@Override
	public void increaseViewCount(Long boardId) {
		boardJpaRepository.increaseViewCount(boardId);
	}
}
