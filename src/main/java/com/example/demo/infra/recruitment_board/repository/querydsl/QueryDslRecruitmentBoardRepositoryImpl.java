package com.example.demo.infra.recruitment_board.repository.querydsl;

import com.example.demo.domain.board.service.entity.vo.Status;
import com.example.demo.domain.recruitment_board.entity.RecruitmentBoardInfo;
import com.example.demo.domain.recruitment_board.entity.vo.RecruitmentBoardType;
import com.example.demo.global.utils.QueryDslUtils;
import com.example.demo.infra.recruitment_board.entity.RecruitmentBoard;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
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

import static com.example.demo.domain.user.domain.QUser.user;
import static com.example.demo.infra.comment.entity.QRecruitmentBoardComment.recruitmentBoardComment;
import static com.example.demo.infra.recruitment_board.entity.QRecruitmentBoard.recruitmentBoard;
import static com.example.demo.infra.recruitment_board.entity.QRecruitmentFormAnswer.recruitmentFormAnswer;
import static com.example.demo.infra.recruitment_board.entity.QRecruitmentFormQuestion.recruitmentFormQuestion;
import static org.springframework.util.ObjectUtils.isEmpty;

@RequiredArgsConstructor
public class QueryDslRecruitmentBoardRepositoryImpl implements QueryDslRecruitmentBoardRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<RecruitmentBoardInfo> findPublishedPageByNoOffset(int size, RecruitmentBoardInfo lastBoardInfo, RecruitmentBoardType boardType) {
        // TODO : 차단 유저 ID에 해당하는 게시물은 가져오지 않게 수정
        // lastBoard보다 마감일이 늦는 게시물을 가져온다.
        return jpaQueryFactory
                .select(Projections.constructor(RecruitmentBoardInfo.class,
                        recruitmentBoardComment.count(),
                        recruitmentBoard.id,
                        recruitmentBoard.user.id, recruitmentBoard.user.nickname,
                        recruitmentBoard.title, recruitmentBoard.summary, recruitmentBoard.host,
                        recruitmentBoard.content, recruitmentBoard.type, recruitmentBoard.tag,
                        recruitmentBoard.status, recruitmentBoard.recruitmentTarget,
                        recruitmentBoard.recruitmentNum, recruitmentBoard.currentMemberNum,
                        recruitmentBoard.recruitmentDeadline, recruitmentBoard.activityStart,
                        recruitmentBoard.activityFinish, recruitmentBoard.activityCycle,
                        recruitmentBoard.createdAt))
                .from(recruitmentBoard)
                .join(recruitmentBoard.user, user)
                .leftJoin(recruitmentBoard.commentList, recruitmentBoardComment)
                .where(getBasicWhereCondition(boardType, Status.PUBLISHED)
                        .and(lastBoardInfo == null ? Expressions.TRUE :
                                (recruitmentBoard.recruitmentDeadline.eq(lastBoardInfo.getRecruitmentDeadline())
                                        .and(recruitmentBoard.id.gt(lastBoardInfo.getBoardId())))
                                        .or(recruitmentBoard.recruitmentDeadline.gt(lastBoardInfo.getRecruitmentDeadline()))
                        ), isActive(null)
                )
//                .where(recruitment.user.id.ne(userId))
                .groupBy(recruitmentBoard.id)
                .orderBy(recruitmentBoard.recruitmentDeadline.asc(), recruitmentBoard.id.asc())
                .limit(size + 1)
                .fetch();
    }

    @Override
    public List<RecruitmentBoard> findDraftPageByUserIdByNoOffset(Long userId, int size, Long lastBoardId) {
        // lastBoardId보다 작성날이 이른 임시저장 게시물을 가져온다.
        return jpaQueryFactory
                .selectFrom(recruitmentBoard)
                .join(recruitmentBoard.user, user)
                .where(recruitmentBoard.status.eq(Status.DRAFT)
                                .and(lastBoardId == null ? Expressions.TRUE : recruitmentBoard.id.lt(lastBoardId)),
                        userEq(userId), isActive(userId))
                .orderBy(recruitmentBoard.createdAt.desc())
                // nextPage 여부를 판단하기 위해 + 1
                .limit(size + 1)
                .fetch();
    }

    @Override
    public Page<RecruitmentBoardInfo> findPublishedPageByPageNum(Long userId, Pageable pageable, RecruitmentBoardType boardType) {
        // userId가 null이 아니면, userId가 작성한 게시물만 가져온다. + 마감일이 지나지 않은 게시물만 가져온다.
        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        BooleanBuilder whereCondition = getBasicWhereCondition(boardType, Status.PUBLISHED);

        List<RecruitmentBoardInfo> content = jpaQueryFactory
                .select(Projections.constructor(RecruitmentBoardInfo.class,
                        recruitmentBoardComment.count(),
                        recruitmentBoard.id,
                        recruitmentBoard.user.id, recruitmentBoard.user.nickname,
                        recruitmentBoard.title, recruitmentBoard.summary, recruitmentBoard.host,
                        recruitmentBoard.content, recruitmentBoard.type, recruitmentBoard.tag,
                        recruitmentBoard.status, recruitmentBoard.recruitmentTarget,
                        recruitmentBoard.recruitmentNum, recruitmentBoard.currentMemberNum,
                        recruitmentBoard.recruitmentDeadline, recruitmentBoard.activityStart,
                        recruitmentBoard.activityFinish, recruitmentBoard.activityCycle,
                        recruitmentBoard.createdAt))
                .from(recruitmentBoard)
                .join(recruitmentBoard.user, user)
                .leftJoin(recruitmentBoard.commentList, recruitmentBoardComment)
                .where(whereCondition, userEq(userId), isActive(userId))
                .groupBy(recruitmentBoard.id)
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long totalCount = jpaQueryFactory
                .select(recruitmentBoard.count())
                .from(recruitmentBoard)
                .where(whereCondition, userEq(userId), isActive(userId))
                .fetchOne();

        return new PageImpl<>(content, pageable, totalCount);
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

    private BooleanExpression userEq(Long userId) {
        return userId != null ? recruitmentBoard.user.id.eq(userId) : null;
    }

    private BooleanExpression isActive(Long userId) {
        return userId == null ? recruitmentBoard.recruitmentDeadline.goe(LocalDateTime.now()) : null;
    }

    public BooleanBuilder getBasicWhereCondition(RecruitmentBoardType boardType, Status status) {
        BooleanBuilder whereCondition = new BooleanBuilder();
        whereCondition.and(recruitmentBoard.type.eq(boardType));
        whereCondition.and(recruitmentBoard.status.eq(status));
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
