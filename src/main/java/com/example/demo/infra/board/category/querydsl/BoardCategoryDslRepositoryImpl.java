package com.example.demo.infra.board.category.querydsl;

import org.springframework.stereotype.Repository;

import com.example.demo.domain.user.domain.QUser;
import com.example.demo.infra.board.category.entity.BoardCategory;
import com.example.demo.infra.board.category.entity.QBoardCategory;
import com.example.demo.infra.board.category.entity.QCategory;
import com.example.demo.infra.board.entity.QBoard;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BoardCategoryDslRepositoryImpl implements BoardCategoryDslRepository {
	private final JPAQueryFactory queryFactory;
	private final QBoard board = QBoard.board;
	private final QBoardCategory boardCategory = QBoardCategory.boardCategory;
	private final QCategory category = QCategory.category;

	@Override
	public BoardCategory findBoardCategoryFetchjoinBoardAndCategory(Long boardId, String categoryName) {
		return queryFactory
			.selectFrom(boardCategory)
			.join(boardCategory.board, board).fetchJoin()
			.join(boardCategory.category, category).fetchJoin()
			.where(board.id.eq(boardId).and(category.name.eq(categoryName)))
			.fetchOne();
	}
}
