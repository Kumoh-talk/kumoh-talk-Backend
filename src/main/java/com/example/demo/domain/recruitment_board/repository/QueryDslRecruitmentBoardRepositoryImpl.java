package com.example.demo.domain.recruitment_board.repository;

import com.example.demo.domain.board.domain.dto.vo.Status;
import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentBoard;
import com.example.demo.domain.recruitment_board.domain.vo.RecruitmentBoardType;
import com.example.demo.global.utils.QueryDslUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.demo.domain.recruitment_board.domain.entity.QRecruitmentBoard.recruitmentBoard;
import static com.example.demo.domain.recruitment_board.domain.entity.QRecruitmentFormAnswer.recruitmentFormAnswer;
import static com.example.demo.domain.recruitment_board.domain.entity.QRecruitmentFormQuestion.recruitmentFormQuestion;
import static com.example.demo.domain.user.domain.QUser.user;
import static org.springframework.util.ObjectUtils.isEmpty;

@RequiredArgsConstructor
public class QueryDslRecruitmentBoardRepositoryImpl implements QueryDslRecruitmentBoardRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<RecruitmentBoard> findPublishedPageByNoOffset(int size, RecruitmentBoard lastBoard, RecruitmentBoardType boardType, boolean isFirst) {
        // TODO : 차단 유저 ID에 해당하는 게시물은 가져오지 않게 수정
        // 처음 페이지는 lastBoardId 이하 게시물을 가져온다.
        // 처음이 아닌 페이지는 lastBoardId 미만 게시물을 가져온다.
        if (isFirst) {
            return jpaQueryFactory
                    .selectFrom(recruitmentBoard)
                    .join(recruitmentBoard.user, user).fetchJoin()
                    .where(recruitmentBoard.recruitmentDeadline.goe(LocalDateTime.now()),
                            recruitmentBoard.recruitmentDeadline.goe(lastBoard.getRecruitmentDeadline()),
                            recruitmentBoard.type.eq(boardType),
                            recruitmentBoard.status.eq(Status.PUBLISHED))
//                .where(recruitment.user.id.ne(userId))
                    .orderBy(recruitmentBoard.recruitmentDeadline.asc(), recruitmentBoard.id.asc())
                    .limit(size + 1)
                    .fetch();
        } else {
            return jpaQueryFactory
                    .selectFrom(recruitmentBoard)
                    .join(recruitmentBoard.user, user).fetchJoin()
                    .where(recruitmentBoard.recruitmentDeadline.goe(LocalDateTime.now())
                            .and(recruitmentBoard.type.eq(boardType))
                            .and(recruitmentBoard.status.eq(Status.PUBLISHED))
                            .and(
                                    recruitmentBoard.recruitmentDeadline.eq(lastBoard.getRecruitmentDeadline())
                                            .and(recruitmentBoard.id.gt(lastBoard.getId()))
                                            .or(recruitmentBoard.recruitmentDeadline.gt(lastBoard.getRecruitmentDeadline()))
                            )
                    )
//                .where(recruitment.user.id.ne(userId))
                    .orderBy(recruitmentBoard.recruitmentDeadline.asc(), recruitmentBoard.id.asc())
                    .limit(size + 1)
                    .fetch();
        }

    }

    @Override
    public Page<RecruitmentBoard> findPublishedPageByPageNum(Pageable pageable, RecruitmentBoardType boardType) {
        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        List<RecruitmentBoard> content = jpaQueryFactory
                .selectFrom(recruitmentBoard)
                .join(recruitmentBoard.user, user).fetchJoin()
                .where(recruitmentBoard.type.eq(boardType),
                        recruitmentBoard.status.eq(Status.PUBLISHED),
                        recruitmentBoard.recruitmentDeadline.goe(LocalDateTime.now()))
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long totalCount = jpaQueryFactory
                .select(recruitmentBoard.count())
                .from(recruitmentBoard)
                .where(recruitmentBoard.type.eq(boardType),
                        recruitmentBoard.status.eq(Status.PUBLISHED),
                        recruitmentBoard.recruitmentDeadline.goe(LocalDateTime.now()))
                .fetchOne();

        return new PageImpl<>(content, pageable, totalCount);
    }

    @Override
    public Page<RecruitmentBoard> findPublishedPageByUserIdByPageNum(Long userId, Pageable pageable, RecruitmentBoardType boardType) {
        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        List<RecruitmentBoard> content = jpaQueryFactory
                .selectFrom(recruitmentBoard)
                .join(recruitmentBoard.user, user).fetchJoin()
                .where(recruitmentBoard.type.eq(boardType),
                        recruitmentBoard.status.eq(Status.PUBLISHED),
                        recruitmentBoard.user.id.eq(userId))
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long totalCount = jpaQueryFactory
                .select(recruitmentBoard.count())
                .from(recruitmentBoard)
                .where(recruitmentBoard.type.eq(boardType),
                        recruitmentBoard.status.eq(Status.PUBLISHED),
                        recruitmentBoard.user.id.eq(userId))
                .fetchOne();

        return new PageImpl<>(content, pageable, totalCount);
    }

    @Override
    public List<RecruitmentBoard> findDraftPageByUserIdByNoOffset(Long userId, int size, Long lastBoardId, boolean isFirst) {
        // 처음 페이지는 lastBoardId 이하 게시물을 가져온다.
        // 처음이 아닌 페이지는 lastBoardId 미만 게시물을 가져온다.
        if (isFirst) {
            return jpaQueryFactory
                    .selectFrom(recruitmentBoard)
                    .join(recruitmentBoard.user, user).fetchJoin()
                    .where(recruitmentBoard.id.loe(lastBoardId),
                            recruitmentBoard.status.eq(Status.DRAFT),
                            recruitmentBoard.user.id.eq(userId))
                    .orderBy(recruitmentBoard.createdAt.desc())
                    // nextPage 여부를 판단하기 위해 + 1
                    .limit(size + 1)
                    .fetch();
        } else {
            return jpaQueryFactory
                    .selectFrom(recruitmentBoard)
                    .join(recruitmentBoard.user, user).fetchJoin()
                    .where(recruitmentBoard.id.lt(lastBoardId),
                            recruitmentBoard.status.eq(Status.DRAFT),
                            recruitmentBoard.user.id.eq(userId))
                    .orderBy(recruitmentBoard.createdAt.desc())
                    // nextPage 여부를 판단하기 위해 + 1
                    .limit(size + 1)
                    .fetch();
        }
    }

    @Override
    public Optional<RecruitmentBoard> findByIdByFetchingQuestionList(Long recruitmentBoardId) {
        RecruitmentBoard result = jpaQueryFactory
                .selectFrom(recruitmentBoard)
                .leftJoin(recruitmentBoard.recruitmentFormQuestionList, recruitmentFormQuestion).fetchJoin()
                .where(recruitmentBoard.id.eq(recruitmentBoardId))
                .fetchOne();

        if (result == null) {
            return Optional.empty();
        }
        Long selectedId = result.getId();

        jpaQueryFactory
                .selectFrom(recruitmentFormQuestion)
                .leftJoin(recruitmentFormQuestion.recruitmentFormAnswerList, recruitmentFormAnswer).fetchJoin()
                .where(recruitmentFormQuestion.recruitmentBoard.id.eq(selectedId))
                .fetch();

        return Optional.of(result);
    }

    private List<OrderSpecifier> getAllOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier> orderSpecifierList = new ArrayList<>();

        if (!isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()) {
                    case "createdAt":
                        OrderSpecifier<?> orderId = QueryDslUtils.getSortedColumn(direction, recruitmentBoard, "createdAt");
                        orderSpecifierList.add(orderId);
                        break;
                    case "recruitmentDeadline":
                        OrderSpecifier<?> orderUser = QueryDslUtils.getSortedColumn(direction, recruitmentBoard, "recruitmentDeadline");
                        orderSpecifierList.add(orderUser);
                        break;
                    default:
                        break;
                }
            }
        }

        return orderSpecifierList;
    }
}
