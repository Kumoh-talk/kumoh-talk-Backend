package com.example.demo.domain.study_project_application.repository;

import com.example.demo.domain.study_project_application.domain.entity.StudyProjectApplicant;
import com.example.demo.domain.study_project_board.domain.dto.vo.StudyProjectBoardType;
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

import static com.example.demo.domain.study_project_application.domain.entity.QStudyProjectApplicant.studyProjectApplicant;
import static com.example.demo.domain.study_project_board.domain.entity.QStudyProjectBoard.studyProjectBoard;
import static org.springframework.util.ObjectUtils.isEmpty;

@RequiredArgsConstructor
public class QueryDslStudyProjectApplicantRepositoryImpl implements QueryDslStudyProjectApplicantRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<StudyProjectApplicant> findByUser_IdFetchStudyProjectBoard(Long userId, Pageable pageable, StudyProjectBoardType boardType) {
        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        List<StudyProjectApplicant> content = jpaQueryFactory
                .selectFrom(studyProjectApplicant)
                .join(studyProjectApplicant.studyProjectBoard, studyProjectBoard).fetchJoin()
                .where(studyProjectApplicant.studyProjectBoard.type.eq(boardType),
                        studyProjectApplicant.user.id.eq(userId))
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long totalCount = jpaQueryFactory
                .select(studyProjectApplicant.count())
                .from(studyProjectApplicant)
                .where(studyProjectApplicant.studyProjectBoard.type.eq(boardType),
                        studyProjectApplicant.user.id.eq(userId))
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
                        OrderSpecifier<?> orderId = QueryDslUtils.getSortedColumn(direction, studyProjectApplicant, "createdAt");
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
