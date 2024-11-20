package com.example.demo.domain.recruitment_board.repository;

import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentFormQuestion;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.demo.domain.recruitment_board.domain.entity.QRecruitmentFormQuestion.recruitmentFormQuestion;

@RequiredArgsConstructor
public class QueryDslRecruitmentFormQuestionRepositoryImpl implements QueryDslRecruitmentFormQuestionRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<RecruitmentFormQuestion> findByBoard_IdByFetchingAnswerList(Long recruitmentBoardId) {
        return jpaQueryFactory
                .selectFrom(recruitmentFormQuestion)
                .leftJoin(recruitmentFormQuestion.recruitmentFormAnswerList).fetchJoin()
                .where(recruitmentFormQuestion.recruitmentBoard.id.eq(recruitmentBoardId))
                .fetch();
    }
}
