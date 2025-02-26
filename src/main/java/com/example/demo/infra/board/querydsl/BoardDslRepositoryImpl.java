package com.example.demo.infra.board.querydsl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.example.demo.application.board.dto.vo.BoardType;
import com.example.demo.application.board.dto.vo.Status;
import com.example.demo.domain.board.service.entity.BoardTitleInfo;
import com.example.demo.domain.user.domain.QUser;
import com.example.demo.infra.board.category.entity.QBoardCategory;
import com.example.demo.infra.board.category.entity.QCategory;
import com.example.demo.infra.board.entity.Board;
import com.example.demo.infra.board.entity.QBoard;
import com.example.demo.infra.board.entity.QLike;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
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
			.leftJoin(boardCategory).on(board.id.eq(boardCategory.board.id)).fetchJoin()
			.leftJoin(category).on(boardCategory.category.id.eq(category.id)).fetchJoin()
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

	public Page<BoardTitleInfo> findBoardByPage(BoardType boardType, Pageable pageable) {
		QBoard board = QBoard.board;
		QLike like = QLike.like;

		JPQLQuery<BoardTitleInfo> contentQuery = createContentQuery(boardType, board, like);
		List<BoardTitleInfo> content = fetchPagedContent(contentQuery, pageable);

		JPQLQuery<Long> countQuery = createCountQuery(boardType, board, like);
		long total = countQuery.fetch().size();

		return new PageImpl<>(content, pageable, total);
	}

	//TODO : 최적화 필요
	private JPQLQuery<BoardTitleInfo> createContentQuery(BoardType boardType, QBoard board, QLike like) {
		return queryFactory
			.select(Projections.constructor(BoardTitleInfo.class,
				board.id,
				board.title,
				board.user.nickname,
				board.boardType,
				board.viewCount,
				like.countDistinct(),
				board.headImageUrl,
				board.createdAt
			))
			.from(board)
			.leftJoin(board.likes, like)
			.where(buildWhereCondition(boardType, board))
			.groupBy(board.id, board.title, board.user.nickname, board.boardType, board.createdAt);
	}

	private JPQLQuery<Long> createCountQuery(BoardType boardType, QBoard board, QLike like) {
		return queryFactory
			.select(board.id.countDistinct())
			.from(board)
			.leftJoin(board.likes, like)
			.where(buildWhereCondition(boardType, board))
			.groupBy(board.id, board.title, board.user.nickname, board.boardType, board.createdAt);
	}

	private BooleanExpression buildWhereCondition(BoardType boardType, QBoard board) {
		return board.status.eq(Status.PUBLISHED)
			.and(board.boardType.eq(boardType));
	}

	private List<BoardTitleInfo> fetchPagedContent(JPQLQuery<BoardTitleInfo> query, Pageable pageable) {
		return query.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
	}
}
