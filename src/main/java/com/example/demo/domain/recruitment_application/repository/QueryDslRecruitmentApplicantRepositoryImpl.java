package com.example.demo.domain.recruitment_application.repository;

import com.example.demo.domain.recruitment_application.domain.entity.RecruitmentApplicant;
import com.example.demo.domain.recruitment_board.domain.dto.vo.RecruitmentBoardType;
import com.example.demo.global.utils.QueryDslUtils;
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

import static com.example.demo.domain.recruitment_application.domain.entity.QRecruitmentApplicant.recruitmentApplicant;
import static com.example.demo.domain.recruitment_board.domain.entity.QRecruitmentBoard.recruitmentBoard;
import static org.springframework.util.ObjectUtils.isEmpty;

@RequiredArgsConstructor
public class QueryDslRecruitmentApplicantRepositoryImpl implements QueryDslRecruitmentApplicantRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<RecruitmentApplicant> findByUser_IdFetchRecruitmentBoard(Long userId, Pageable pageable, RecruitmentBoardType boardType) {
        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        List<RecruitmentApplicant> content = jpaQueryFactory
                .selectFrom(recruitmentApplicant)
                .join(recruitmentApplicant.recruitmentBoard, recruitmentBoard).fetchJoin()
                .where(recruitmentApplicant.recruitmentBoard.type.eq(boardType),
                        recruitmentApplicant.user.id.eq(userId))
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long totalCount = jpaQueryFactory
                .select(recruitmentApplicant.count())
                .from(recruitmentApplicant)
                .where(recruitmentApplicant.recruitmentBoard.type.eq(boardType),
                        recruitmentApplicant.user.id.eq(userId))
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
                        OrderSpecifier<?> orderId = QueryDslUtils.getSortedColumn(direction, recruitmentApplicant, "createdAt");
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
