package com.example.demo.domain.recruitment_board.repository;

import com.example.demo.domain.board.service.entity.vo.Status;
import com.example.demo.domain.recruitment_board.domain.dto.response.RecruitmentBoardNoOffsetResponse;
import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentBoard;
import com.example.demo.domain.recruitment_board.domain.vo.RecruitmentBoardType;
import com.example.demo.global.utils.QueryDslUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
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

import static com.example.demo.domain.comment.domain.entity.QRecruitmentBoardComment.recruitmentBoardComment;
import static com.example.demo.domain.recruitment_board.domain.entity.QRecruitmentBoard.recruitmentBoard;
import static com.example.demo.domain.recruitment_board.domain.entity.QRecruitmentFormAnswer.recruitmentFormAnswer;
import static com.example.demo.domain.recruitment_board.domain.entity.QRecruitmentFormQuestion.recruitmentFormQuestion;
import static com.example.demo.domain.user.domain.QUser.user;
import static org.springframework.util.ObjectUtils.isEmpty;

@RequiredArgsConstructor
public class QueryDslRecruitmentBoardRepositoryImpl implements QueryDslRecruitmentBoardRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<RecruitmentBoardNoOffsetResponse.RecruitmentBoardSummaryInfo> findPublishedPageByNoOffset(int size, RecruitmentBoard lastBoard, RecruitmentBoardType boardType) {
        // TODO : 차단 유저 ID에 해당하는 게시물은 가져오지 않게 수정
        // lastBoard보다 마감일이 늦는 게시물을 가져온다.
        return jpaQueryFactory
                .select(Projections.constructor(RecruitmentBoardNoOffsetResponse.RecruitmentBoardSummaryInfo.class,
                        recruitmentBoardComment.count(),
                        recruitmentBoard.id, recruitmentBoard.title,
                        recruitmentBoard.summary, recruitmentBoard.type,
                        recruitmentBoard.tag, recruitmentBoard.recruitmentTarget,
                        recruitmentBoard.recruitmentNum, recruitmentBoard.currentMemberNum,
                        recruitmentBoard.createdAt, recruitmentBoard.recruitmentDeadline))
                .from(recruitmentBoard)
//                .join(recruitmentBoard.user, user)
                .leftJoin(recruitmentBoard.commentList, recruitmentBoardComment)
                .where(getBasicWhereCondition(boardType, Status.PUBLISHED, true)
                        .and(lastBoard == null ? Expressions.TRUE :
                                (recruitmentBoard.recruitmentDeadline.eq(lastBoard.getRecruitmentDeadline())
                                        .and(recruitmentBoard.id.gt(lastBoard.getId())))
                                        .or(recruitmentBoard.recruitmentDeadline.gt(lastBoard.getRecruitmentDeadline()))
                        )
                )
//                .where(recruitment.user.id.ne(userId))
                .groupBy(recruitmentBoard.id)
                .orderBy(recruitmentBoard.recruitmentDeadline.asc(), recruitmentBoard.id.asc())
                .limit(size + 1)
                .fetch();
    }

    @Override
    public Page<RecruitmentBoardNoOffsetResponse.RecruitmentBoardSummaryInfo> findPublishedPageByPageNum(Pageable pageable, RecruitmentBoardType boardType) {
        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        BooleanBuilder whereCondition = getBasicWhereCondition(boardType, Status.PUBLISHED, true);

        List<RecruitmentBoardNoOffsetResponse.RecruitmentBoardSummaryInfo> content = jpaQueryFactory
                .select(Projections.constructor(RecruitmentBoardNoOffsetResponse.RecruitmentBoardSummaryInfo.class,
                        recruitmentBoardComment.count(),
                        recruitmentBoard.id, recruitmentBoard.title,
                        recruitmentBoard.summary, recruitmentBoard.type,
                        recruitmentBoard.tag, recruitmentBoard.recruitmentTarget,
                        recruitmentBoard.recruitmentNum, recruitmentBoard.currentMemberNum,
                        recruitmentBoard.createdAt, recruitmentBoard.recruitmentDeadline))
                .from(recruitmentBoard)
                .leftJoin(recruitmentBoard.commentList, recruitmentBoardComment)
                .where(whereCondition)
                .groupBy(recruitmentBoard.id)
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long totalCount = jpaQueryFactory
                .select(recruitmentBoard.count())
                .from(recruitmentBoard)
                .where(whereCondition)
                .fetchOne();

        return new PageImpl<>(content, pageable, totalCount);
    }

    @Override
    public Page<RecruitmentBoardNoOffsetResponse.RecruitmentBoardSummaryInfo> findPublishedPageByUserIdByPageNum(Long userId, Pageable pageable, RecruitmentBoardType boardType) {
        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        List<RecruitmentBoardNoOffsetResponse.RecruitmentBoardSummaryInfo> content = jpaQueryFactory
                .select(Projections.constructor(RecruitmentBoardNoOffsetResponse.RecruitmentBoardSummaryInfo.class,
                        recruitmentBoardComment.count(),
                        recruitmentBoard.id, recruitmentBoard.title,
                        recruitmentBoard.summary, recruitmentBoard.type,
                        recruitmentBoard.tag, recruitmentBoard.recruitmentTarget,
                        recruitmentBoard.recruitmentNum, recruitmentBoard.currentMemberNum,
                        recruitmentBoard.createdAt, recruitmentBoard.recruitmentDeadline))
                .from(recruitmentBoard)
                .leftJoin(recruitmentBoard.commentList, recruitmentBoardComment)
                .join(recruitmentBoard.user, user)
                .where(getBasicWhereCondition(boardType, Status.PUBLISHED, true)
                        .and(recruitmentBoard.user.id.eq(userId)))
                .groupBy(recruitmentBoard.id)
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long totalCount = jpaQueryFactory
                .select(recruitmentBoard.count())
                .from(recruitmentBoard)
                .where(getBasicWhereCondition(boardType, Status.PUBLISHED, true)
                        .and(recruitmentBoard.user.id.eq(userId)))
                .fetchOne();

        return new PageImpl<>(content, pageable, totalCount);
    }

    @Override
    public List<RecruitmentBoard> findDraftPageByUserIdByNoOffset(Long userId, int size, Long lastBoardId) {
        // 처음 페이지는 lastBoardId 이하 게시물을 가져온다.
        // 처음이 아닌 페이지는 lastBoardId 미만 게시물을 가져온다.
        return jpaQueryFactory
                .selectFrom(recruitmentBoard)
                .join(recruitmentBoard.user, user)
                .where(recruitmentBoard.status.eq(Status.DRAFT)
                        .and(recruitmentBoard.user.id.eq(userId))
                        .and(lastBoardId == null ? Expressions.TRUE : recruitmentBoard.id.lt(lastBoardId)))
                .orderBy(recruitmentBoard.createdAt.desc())
                // nextPage 여부를 판단하기 위해 + 1
                .limit(size + 1)
                .fetch();
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

    public BooleanBuilder getBasicWhereCondition(RecruitmentBoardType boardType, Status status, boolean shouldActive) {
        BooleanBuilder whereCondition = new BooleanBuilder();
        whereCondition.and(recruitmentBoard.type.eq(boardType));
        whereCondition.and(recruitmentBoard.status.eq(status));
        if (shouldActive) {
            whereCondition.and(recruitmentBoard.recruitmentDeadline.goe(LocalDateTime.now()));
        }
        return whereCondition;
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
