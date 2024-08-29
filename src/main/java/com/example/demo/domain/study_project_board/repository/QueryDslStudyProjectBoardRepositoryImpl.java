package com.example.demo.domain.study_project_board.repository;

import com.example.demo.domain.board.domain.dto.vo.Status;
import com.example.demo.domain.study_project_board.domain.dto.vo.StudyProjectBoardCategory;
import com.example.demo.domain.study_project_board.domain.entity.StudyProjectBoard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.demo.domain.study_project_board.domain.entity.QStudyProjectBoard.studyProjectBoard;
import static com.example.demo.domain.user.domain.QUser.user;

@RequiredArgsConstructor
public class QueryDslStudyProjectBoardRepositoryImpl implements QueryDslStudyProjectBoardRepository {
    private final JPAQueryFactory jpaQueryFactory;

    // TODO : pageNum 방식 페이징에 정렬 부분 하드코딩 제거

    @Override
    public List<StudyProjectBoard> findPublishedPageByNoOffset(int size, StudyProjectBoard lastBoard, StudyProjectBoardCategory category, boolean isFirst) {
        // TODO : 차단 유저 ID에 해당하는 게시물은 가져오지 않게 수정
        // 처음 페이지는 lastBoardId 이하 게시물을 가져온다.
        // 처음이 아닌 페이지는 lastBoardId 미만 게시물을 가져온다.
        if (isFirst) {
            return jpaQueryFactory
                    .selectFrom(studyProjectBoard)
                    .join(studyProjectBoard.user, user).fetchJoin()
                    .where(studyProjectBoard.recruitmentDeadline.goe(LocalDateTime.now()),
                            studyProjectBoard.recruitmentDeadline.goe(lastBoard.getRecruitmentDeadline()),
                            studyProjectBoard.category.eq(category),
                            studyProjectBoard.status.eq(Status.PUBLISHED))
//                .where(studyProjectBoard.user.id.ne(userId))
                    .orderBy(studyProjectBoard.recruitmentDeadline.asc())
                    .limit(size + 1)
                    .fetch();
        } else {
            return jpaQueryFactory
                    .selectFrom(studyProjectBoard)
                    .join(studyProjectBoard.user, user).fetchJoin()
                    .where(studyProjectBoard.recruitmentDeadline.goe(LocalDateTime.now()),
                            studyProjectBoard.recruitmentDeadline.goe(lastBoard.getRecruitmentDeadline()),
                            studyProjectBoard.id.ne(lastBoard.getId()),
                            studyProjectBoard.category.eq(category),
                            studyProjectBoard.status.eq(Status.PUBLISHED))
//                .where(studyProjectBoard.user.id.ne(userId))
                    .orderBy(studyProjectBoard.recruitmentDeadline.asc())
                    .limit(size + 1)
                    .fetch();
        }

    }

    @Override
    public Page<StudyProjectBoard> findPublishedPageByPageNum(Pageable pageable, StudyProjectBoardCategory category) {
        List<StudyProjectBoard> content = jpaQueryFactory
                .selectFrom(studyProjectBoard)
                .join(studyProjectBoard.user, user).fetchJoin()
                .where(studyProjectBoard.category.eq(category),
                        studyProjectBoard.status.eq(Status.PUBLISHED),
                        studyProjectBoard.recruitmentDeadline.goe(LocalDateTime.now()))
                .orderBy(studyProjectBoard.recruitmentDeadline.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long totalCount = jpaQueryFactory
                .select(studyProjectBoard.count())
                .from(studyProjectBoard)
                .where(studyProjectBoard.category.eq(category),
                        studyProjectBoard.status.eq(Status.PUBLISHED),
                        studyProjectBoard.recruitmentDeadline.goe(LocalDateTime.now()))
                .fetchOne();

        return new PageImpl<>(content, pageable, totalCount);
    }

    @Override
    public Page<StudyProjectBoard> findPublishedPageByUserIdByPageNum(Long userId, Pageable pageable, StudyProjectBoardCategory category) {
        List<StudyProjectBoard> content = jpaQueryFactory
                .selectFrom(studyProjectBoard)
                .join(studyProjectBoard.user, user).fetchJoin()
                .where(studyProjectBoard.category.eq(category),
                        studyProjectBoard.status.eq(Status.PUBLISHED),
                        studyProjectBoard.user.id.eq(userId))
                .orderBy(studyProjectBoard.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long totalCount = jpaQueryFactory
                .select(studyProjectBoard.count())
                .from(studyProjectBoard)
                .where(studyProjectBoard.category.eq(category),
                        studyProjectBoard.status.eq(Status.PUBLISHED),
                        studyProjectBoard.user.id.eq(userId))
                .fetchOne();

        return new PageImpl<>(content, pageable, totalCount);
    }

    @Override
    public List<StudyProjectBoard> findDraftPageByUserIdByNoOffset(Long userId, int size, Long lastBoardId, boolean isFirst) {
        // 처음 페이지는 lastBoardId 이하 게시물을 가져온다.
        // 처음이 아닌 페이지는 lastBoardId 미만 게시물을 가져온다.
        if (isFirst) {
            return jpaQueryFactory
                    .selectFrom(studyProjectBoard)
                    .join(studyProjectBoard.user, user).fetchJoin()
                    .where(studyProjectBoard.id.loe(lastBoardId),
                            studyProjectBoard.status.eq(Status.DRAFT),
                            studyProjectBoard.user.id.eq(userId))
                    .orderBy(studyProjectBoard.createdAt.desc())
                    // nextPage 여부를 판단하기 위해 + 1
                    .limit(size + 1)
                    .fetch();
        } else {
            return jpaQueryFactory
                    .selectFrom(studyProjectBoard)
                    .join(studyProjectBoard.user, user).fetchJoin()
                    .where(studyProjectBoard.id.lt(lastBoardId),
                            studyProjectBoard.status.eq(Status.DRAFT),
                            studyProjectBoard.user.id.eq(userId))
                    .orderBy(studyProjectBoard.createdAt.desc())
                    // nextPage 여부를 판단하기 위해 + 1
                    .limit(size + 1)
                    .fetch();
        }
    }

    @Override
    public Optional<StudyProjectBoard> findByIdByFetchingUser(Long ApplicationBoardId) {
        StudyProjectBoard result = jpaQueryFactory
                .selectFrom(studyProjectBoard)
                .join(studyProjectBoard.user, user).fetchJoin()
                .leftJoin(studyProjectBoard.studyProjectFormQuestionList).fetchJoin()
                .where(studyProjectBoard.id.eq(ApplicationBoardId))
                .fetchOne();
        return Optional.ofNullable(result);
    }
}
