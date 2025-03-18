package com.example.demo.infra.board.repository;

import com.example.demo.domain.board.service.entity.BoardTitleInfo;
import com.example.demo.domain.board.service.entity.LikeInfo;
import com.example.demo.domain.board.service.entity.vo.Status;
import com.example.demo.domain.board.service.repository.LikeRepository;
import com.example.demo.infra.board.entity.Board;
import com.example.demo.infra.board.entity.Like;
import com.example.demo.infra.board.entity.QBoard;
import com.example.demo.infra.board.entity.QLike;
import com.example.demo.infra.user.entity.QUser;
import com.example.demo.infra.user.entity.User;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepository {
    private final LikeJpaRepository likeJpaRepository;
    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    @Override
    public LikeInfo saveLike(Long userId, Long boardId) {
        Like like = new Like(new User(userId), new Board(boardId));
        Like saveLike = likeJpaRepository.save(like);
        return LikeInfo.builder()
                .likeId(saveLike.getId())
                .userId(saveLike.getUser().getId())
                .boardId(saveLike.getBoard().getId())
                .build();
    }

    @Override
    public boolean existsByBoardIdAndUserId(Long boardId, Long userId) {
        return likeJpaRepository.existsByBoardIdAndUserId(boardId, userId);
    }

    @Override
    public void deleteLike(Long userId, Long boardId) {
        deleteByUserIdAndBoardId(userId, boardId);
    }

    @Override
    public Page<BoardTitleInfo> findLikedBoardPageByUserId(Long userId, Pageable pageable) {
        QBoard board = QBoard.board;
        QLike like = QLike.like;
        QUser user = QUser.user;

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
                .join(like).on(like.board.eq(board))
                .join(board.user, user)
                .where(like.user.id.eq(userId).and(board.status.eq(Status.PUBLISHED)))
                .groupBy(board.id, board.title, user.nickname, board.boardType, board.createdAt);

        QueryResults<BoardTitleInfo> results = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    @Transactional
    @Override
    public List<Long> deleteLikesByBoardId(Long boardId) {
        QLike like = QLike.like;

        List<Long> likeIdList = queryFactory
                .select(like.id)
                .from(like)
                .where(like.board.id.eq(boardId))
                .fetch();

        queryFactory
                .update(like)
                .set(like.deletedAt, LocalDateTime.now())
                .where(like.board.id.eq(boardId))
                .execute();
        return likeIdList;
    }

    @Transactional
    public void deleteByUserIdAndBoardId(Long userId, Long boardId) {
        QLike like = QLike.like;
        queryFactory
                .update(like)
                .set(like.deletedAt, LocalDateTime.now())
                .where(like.user.id.eq(userId).and(like.board.id.eq(boardId)))
                .execute();
        entityManager.flush();
        entityManager.clear();
    }

    @Override
    public Optional<Long> findIdByBoardIdAndUserId(Long boardId, Long userId) {
        return likeJpaRepository.findIdByBoardIdAndUserId(boardId, userId);
    }

}
