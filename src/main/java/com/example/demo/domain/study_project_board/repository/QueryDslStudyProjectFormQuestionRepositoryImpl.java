package com.example.demo.domain.study_project_board.repository;

import com.example.demo.domain.study_project_board.domain.entity.StudyProjectFormQuestion;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.example.demo.domain.study_project_board.domain.entity.QStudyProjectFormQuestion.studyProjectFormQuestion;

@RequiredArgsConstructor
public class QueryDslStudyProjectFormQuestionRepositoryImpl implements QueryDslStudyProjectFormQuestionRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<List<StudyProjectFormQuestion>> findByBoard_IdByFetchingAnswerList(Long applicationBoardId) {
        return Optional.of(jpaQueryFactory
                .selectFrom(studyProjectFormQuestion)
                .leftJoin(studyProjectFormQuestion.studyProjectFormChoiceAnswerList).fetchJoin()
                .where(studyProjectFormQuestion.studyProjectBoard.id.eq(applicationBoardId))
                .fetch());
    }
}
