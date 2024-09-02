package com.example.demo.domain.comment.repository;

import com.example.demo.domain.comment.domain.entity.Comment;
import com.example.demo.domain.comment.domain.entity.QComment;
import com.example.demo.domain.study_project_board.domain.dto.vo.BoardType;
import com.example.demo.domain.study_project_board.domain.dto.vo.StudyProjectBoardType;
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
        QComment replyComment = new QComment("replyComment");

        return jpaQueryFactory
                .selectFrom(comment)
                .join(comment.user, user).fetchJoin()
                .leftJoin(comment.replyComments, replyComment).fetchJoin()
                .where(comment.board.id.eq(boardId), comment.parentComment.isNull())
                .orderBy(comment.createdAt.asc())
                .fetch();
    }

    @Override
    public List<Comment> findByStudyProjectBoard_idOrderByCreatedAtAsc(Long boardId) {
        QComment replyComment = new QComment("replyComment");

        return jpaQueryFactory
                .selectFrom(comment)
                .join(comment.user, user).fetchJoin()
                .leftJoin(comment.replyComments, replyComment).fetchJoin()
                .where(comment.studyProjectBoard.id.eq(boardId), comment.parentComment.isNull())
                .orderBy(comment.createdAt.asc())
                .fetch();
    }

    @Override
    public Page<Comment> findCommentByUser_idOrderByCreatedAtDsc(Pageable pageable, Long userId, BoardType boardType) {
        if (boardType == BoardType.SEMINAR_NOTICE || boardType == BoardType.SEMINAR_SUMMARY) {
            return findSeminarComment(pageable, userId);
        } else {
            return findStudyAndProjectComment(pageable, userId, boardType);
        }
    }

    public Page<Comment> findSeminarComment(Pageable pageable, Long userId) {
        List<Comment> content = jpaQueryFactory
                .selectFrom(comment)
                .join(comment.board, board).fetchJoin()
                .where(comment.board.isNotNull(),
                        comment.user.id.eq(userId),
                        comment.deletedAt.isNull())
                .orderBy(comment.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = jpaQueryFactory
                .select(comment.count())
                .from(comment)
                .where(comment.board.isNotNull(),
                        comment.user.id.eq(userId),
                        comment.deletedAt.isNull())
                .fetchOne();

        return new PageImpl<>(content, pageable, totalCount);
    }

    public Page<Comment> findStudyAndProjectComment(Pageable pageable, Long userId, BoardType boardType) {
        List<Comment> content = jpaQueryFactory
                .selectFrom(comment)
                .join(comment.studyProjectBoard, studyProjectBoard).fetchJoin()
                .where(comment.studyProjectBoard.isNotNull(),
                        comment.studyProjectBoard.type.eq(StudyProjectBoardType.valueOf(boardType.toString())),
                        comment.user.id.eq(userId),
                        comment.deletedAt.isNull())
                .orderBy(comment.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long totalCount = jpaQueryFactory
                .select(comment.count())
                .from(comment)
                .where(comment.studyProjectBoard.isNotNull(),
                        comment.studyProjectBoard.type.eq(StudyProjectBoardType.valueOf(boardType.toString())),
                        comment.user.id.eq(userId),
                        comment.deletedAt.isNull())
                .fetchOne();

        return new PageImpl<>(content, pageable, totalCount);
    }
}
