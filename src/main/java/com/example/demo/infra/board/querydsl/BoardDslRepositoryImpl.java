package com.example.demo.infra.board.querydsl;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.demo.domain.user.domain.QUser;
import com.example.demo.infra.board.entity.Board;
import com.example.demo.infra.board.entity.QBoard;
import com.example.demo.infra.board.entity.QBoardCategory;
import com.example.demo.infra.board.entity.QCategory;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BoardDslRepositoryImpl implements BoardDslRepository {
	private final JPAQueryFactory queryFactory;
	private final QBoard board = QBoard.board;
	private final QBoardCategory boardCategory = QBoardCategory.boardCategory;
	private final QCategory category = QCategory.category;
	private final QUser user = QUser.user;


	public Optional<Board> findBoardAndUserAndCategory(Long boardId) {
		Board result = queryFactory
			.selectFrom(board)
			.join(board.user, user).fetchJoin()
			.leftJoin(board.boardCategories, boardCategory).fetchJoin()
			.leftJoin(boardCategory.category, category).fetchJoin()
			.where(board.id.eq(boardId))
			.fetchOne();

		return Optional.ofNullable(result);
	}

	public void increaseViewCount(Long boardId, Integer viewCount) {
		queryFactory
			.update(board)
			.set(board.viewCount, board.viewCount.add(viewCount))
			.where(board.id.eq(boardId))
			.execute();
	}


}
