package com.example.demo.infra.board.querydsl;

import java.util.List;
import java.util.Optional;

import com.example.demo.infra.user.entity.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.board.service.entity.vo.BoardType;
import com.example.demo.domain.board.service.entity.vo.Status;
import com.example.demo.domain.board.service.entity.BoardTitleInfo;
import com.example.demo.domain.board.service.entity.DraftBoardTitle;
import com.example.demo.infra.board.category.entity.QBoardCategory;
import com.example.demo.infra.board.category.entity.QCategory;
import com.example.demo.infra.board.entity.Board;
import com.example.demo.infra.board.entity.QBoard;
import com.example.demo.infra.board.entity.QLike;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
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
	private final QLike like = QLike.like;

	private final BoardPaggingCreator boardPaggingCreator;


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
		JPQLQuery<BoardTitleInfo> contentQuery = createContentQuery(boardType, board, like);
		List<BoardTitleInfo> content = fetchPagedContent(contentQuery, pageable);

		JPQLQuery<Long> countQuery = createCountQuery(boardType, board, like);
		long total = countQuery.fetch().size();

		List<OrderSpecifier> orderSpecifiers = boardPaggingCreator.createOrderSpecifiers(board, pageable);

		if(!orderSpecifiers.isEmpty()) {
			contentQuery.orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]));
		}


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
				like.id.countDistinct(),
				board.headImageUrl,
				board.createdAt
			))
			.from(board)
			.leftJoin(like).on(like.board.eq(board)) // Like에서 Board를 참조
			.where(buildWhereCondition(boardType, board))
			.groupBy(board.id, board.title, board.user.nickname, board.boardType, board.createdAt);
	}

	private JPQLQuery<Long> createCountQuery(BoardType boardType, QBoard board, QLike like) {
		return queryFactory
			.select(board.id.countDistinct())
			.from(board)
			.leftJoin(like).on(like.board.eq(board)) // Like에서 Board를 참조
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


	//TODO : 최적화 필요
	public Page<DraftBoardTitle> findDraftBoardByPage(Long userId, Pageable pageable) {

		JPQLQuery<DraftBoardTitle> query = queryFactory
			.select(Projections.constructor(DraftBoardTitle.class,
				board.id,
				board.title,
				board.createdAt,
				board.updatedAt
			))
			.from(board)
			.where(board.user.id.eq(userId)
				.and(board.status.eq(Status.DRAFT)))
			;

		List<OrderSpecifier> orderSpecifiers = boardPaggingCreator.createOrderSpecifiers(board, pageable);

		if(!orderSpecifiers.isEmpty()) {
			query.orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]));
		}

		// Pageable 적용
		QueryResults<DraftBoardTitle> results = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetchResults();

		return new PageImpl<>(results.getResults(), pageable, results.getTotal());
	}

	@Override
	public Page<BoardTitleInfo> findPublishedBoardListByUser(Long userId, BoardType boardType, Pageable pageable) {
		JPQLQuery<BoardTitleInfo> query = queryFactory
			.select(Projections.constructor(BoardTitleInfo.class,
				board.id,
				board.title,
				user.nickname,
				board.boardType,
				board.viewCount,
				like.id.countDistinct(),
				board.headImageUrl,
				board.createdAt
			))
			.from(board)
			.leftJoin(like).on(like.board.eq(board)) // Like에서 Board를 참조
			.join(board.user, user)
			.where(
				board.status.eq(Status.PUBLISHED)
					.and(board.user.id.eq(userId))
					.and(board.boardType.eq(boardType))
			)
			.groupBy(board.id, board.title, user.nickname, board.boardType, board.createdAt);

		List<OrderSpecifier> orderSpecifiers = boardPaggingCreator.createOrderSpecifiers(board, pageable);

		if(!orderSpecifiers.isEmpty()) {
			query.orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]));
		}


		// 페이징 적용
		QueryResults<BoardTitleInfo> results = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetchResults();

		return new PageImpl<>(results.getResults(), pageable, results.getTotal());
	}


	@Override
	public String getAttachFileUrl(Long boardId) {
		return queryFactory
			.select(board.headImageUrl)
			.from(board)
			.where(board.id.eq(boardId))
			.fetchOne();
	}

}
