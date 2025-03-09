package com.example.demo.infra.recruitment_application.repository.querydsl;

import com.example.demo.domain.recruitment_board.entity.vo.RecruitmentBoardType;
import com.example.demo.global.utils.QueryDslUtils;
import com.example.demo.infra.recruitment_application.entity.RecruitmentApplication;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.domain.user.domain.QUser.user;
import static com.example.demo.infra.recruitment_application.entity.QRecruitmentApplication.recruitmentApplication;
import static com.example.demo.infra.recruitment_board.entity.QRecruitmentBoard.recruitmentBoard;
import static org.springframework.util.ObjectUtils.isEmpty;

@RequiredArgsConstructor
public class QueryDslRecruitmentApplicationRepositoryImpl implements QueryDslRecruitmentApplicationRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<RecruitmentApplication> findPageByUserIdWithRecruitmentBoard(Long userId, Pageable pageable, RecruitmentBoardType boardType) {
        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        List<RecruitmentApplication> content = jpaQueryFactory
                .selectFrom(recruitmentApplication)
                .join(recruitmentApplication.user, user).fetchJoin()
                .join(recruitmentApplication.recruitmentBoard, recruitmentBoard).fetchJoin()
                .where(recruitmentApplication.recruitmentBoard.type.eq(boardType),
                        recruitmentApplication.user.id.eq(userId))
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long totalCount = jpaQueryFactory
                .select(recruitmentApplication.count())
                .from(recruitmentApplication)
                .where(recruitmentApplication.recruitmentBoard.type.eq(boardType),
                        recruitmentApplication.user.id.eq(userId))
                .fetchOne();

        return new PageImpl<>(content, pageable, totalCount);
    }

    private List<OrderSpecifier> getAllOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier> orderSpecifierList = new ArrayList<>();

        if (!isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()) {
                    case "createdAt":
                        OrderSpecifier<?> orderId = QueryDslUtils.getSortedColumn(direction, recruitmentApplication, "createdAt");
                        orderSpecifierList.add(orderId);
                        break;
                    default:
                        break;
                }
            }
        }

        return orderSpecifierList;
    }
}
