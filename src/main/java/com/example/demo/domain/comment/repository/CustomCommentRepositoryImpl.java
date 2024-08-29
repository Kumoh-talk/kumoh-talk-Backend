package com.example.demo.domain.comment.repository;

import com.example.demo.domain.comment.domain.entity.Comment;
import com.example.demo.domain.study_project_board.domain.dto.vo.BoardCategory;
import com.example.demo.domain.study_project_board.domain.dto.vo.StudyProjectBoardCategory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.example.demo.domain.board.domain.entity.QBoard.board;
import static com.example.demo.domain.comment.domain.entity.QComment.comment;
import static com.example.demo.domain.study_project_board.domain.entity.QStudyProjectBoard.studyProjectBoard;
import static com.example.demo.domain.user.domain.QUser.user;


@RequiredArgsConstructor
public class CustomCommentRepositoryImpl implements CustomCommentRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Comment> findByBoard_idOrderByCreatedAtAsc(Long boardId) {
        return jpaQueryFactory
                .selectFrom(comment)
                .join(comment.user, user).fetchJoin()
                .leftJoin(comment.replyComments).fetchJoin()
                .where(comment.board.id.eq(boardId), comment.parentComment.isNull())
                .orderBy(comment.createdAt.asc())
                .fetch();
    }

    @Override
    public Page<Comment> findCommentByUser_idOrderByCreatedAtDsc(Pageable pageable, Long userId, BoardCategory category) {
        List<Comment> content = null;
        Long totalCount = null;
        if (category == BoardCategory.SEMINAR) {
            findSeminarComment(content, totalCount, pageable, userId);
        } else {
            findStudyAndProjectComment(content, totalCount, pageable, userId, category);
        }

        return new PageImpl<>(content, pageable, totalCount);
    }

    public void findSeminarComment(List<Comment> content, Long totalCount, Pageable pageable, Long userId) {
        content = jpaQueryFactory
                .selectFrom(comment)
                .join(comment.board, board).fetchJoin()
                .where(comment.board.isNotNull(),
                        comment.user.id.eq(userId),
                        comment.deletedAt.isNull())
                .orderBy(comment.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        totalCount = jpaQueryFactory
                .select(comment.count())
                .from(comment)
                .where(comment.board.isNotNull(),
                        comment.user.id.eq(userId),
                        comment.deletedAt.isNull())
                .fetchOne();
    }

    public void findStudyAndProjectComment(List<Comment> content, Long totalCount, Pageable pageable, Long userId, BoardCategory category) {
        content = jpaQueryFactory
                .selectFrom(comment)
                .join(comment.studyProjectBoard, studyProjectBoard).fetchJoin()
                .where(comment.studyProjectBoard.isNotNull(),
                        comment.studyProjectBoard.category.eq(StudyProjectBoardCategory.valueOf(category.toString())),
                        comment.user.id.eq(userId),
                        comment.deletedAt.isNull())
                .orderBy(comment.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        totalCount = jpaQueryFactory
                .select(comment.count())
                .from(comment)
                .where(comment.studyProjectBoard.isNotNull(),
                        comment.studyProjectBoard.category.eq(StudyProjectBoardCategory.valueOf(category.toString())),
                        comment.user.id.eq(userId),
                        comment.deletedAt.isNull())
                .fetchOne();
    }
}
