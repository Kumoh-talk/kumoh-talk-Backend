package com.example.demo.infra.board.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.base.page.GlobalPageableDto;
import com.example.demo.domain.board.service.entity.BoardTitleInfo;
import com.example.demo.domain.board.service.entity.vo.Status;
import com.example.demo.domain.board.service.repository.CategoryRepository;
import com.example.demo.domain.user.domain.QUser;
import com.example.demo.infra.board.category.entity.QBoardCategory;
import com.example.demo.infra.board.category.entity.QCategory;
import com.example.demo.infra.board.category.repository.CategoryJpaRepository;
import com.example.demo.infra.board.entity.QBoard;
import com.example.demo.infra.board.entity.QLike;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {
	private final CategoryJpaRepository categoryJpaRepository;
	private final JPAQueryFactory queryFactory;

	@Override
	public List<String> getCategories() {
		return categoryJpaRepository.findAll().stream()
			.map(category -> category.getName())
			.collect(Collectors.toList());
	}

	@Override
	public GlobalPageableDto<BoardTitleInfo> findBoardPaegByCategoryName(String categoryName,
		GlobalPageableDto pageableDto) {
		Page<BoardTitleInfo> boardByPage = findBoardPageByCategoryName(categoryName, pageableDto.getPageable());
		pageableDto.setPage(boardByPage);
		return pageableDto;
	}

	public Page<BoardTitleInfo> findBoardPageByCategoryName(String categoryName, Pageable pageable) {
		QBoard board = QBoard.board;
		QLike like = QLike.like;
		QUser user = QUser.user;
		QBoardCategory boardCategory = QBoardCategory.boardCategory;
		QCategory category = QCategory.category;

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
			.leftJoin(boardCategory).on(board.id.eq(boardCategory.board.id))
			.leftJoin(category).on(boardCategory.category.id.eq(category.id))
			.where(category.name.eq(categoryName).and(board.status.eq(Status.PUBLISHED)))
			.groupBy(board.id, board.title, user.nickname, board.boardType, board.createdAt);

		// 페이징 적용
		QueryResults<BoardTitleInfo> results = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetchResults();

		return new PageImpl<>(results.getResults(), pageable, results.getTotal());
	}

}
