package com.example.demo.infra.recruitment_board.repository.querydsl;

import com.example.demo.infra.recruitment_board.entity.RecruitmentFormQuestion;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.demo.infra.recruitment_board.entity.QRecruitmentFormQuestion.recruitmentFormQuestion;

@RequiredArgsConstructor
public class QueryDslRecruitmentFormQuestionRepositoryImpl implements QueryDslRecruitmentFormQuestionRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<RecruitmentFormQuestion> findByBoardIdWithAnswerList(Long recruitmentBoardId) {
        return jpaQueryFactory
                .selectFrom(recruitmentFormQuestion)
                .leftJoin(recruitmentFormQuestion.recruitmentFormAnswerList).fetchJoin()
                .where(recruitmentFormQuestion.recruitmentBoard.id.eq(recruitmentBoardId))
                .fetch();
    }
}
