package com.example.demo.domain.comment.repository;

import com.example.demo.domain.comment.domain.entity.Comment;
import com.example.demo.domain.comment.domain.entity.QComment;
import com.example.demo.domain.recruitment_board.domain.vo.EntireBoardType;
import com.example.demo.domain.recruitment_board.domain.vo.RecruitmentBoardType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.example.demo.domain.board.domain.entity.QBoard.board;
import static com.example.demo.domain.comment.domain.entity.QComment.comment;
import static com.example.demo.domain.recruitment_board.domain.entity.QRecruitmentBoard.recruitmentBoard;
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
    public List<Comment> findByRecruitmentBoard_idOrderByCreatedAtAsc(Long recruitmentBoardId) {
        QComment replyComment = new QComment("replyComment");

        return jpaQueryFactory
                .selectFrom(comment)
                .join(comment.user, user).fetchJoin()
                .leftJoin(comment.replyComments, replyComment).fetchJoin()
                .where(comment.recruitmentBoard.id.eq(recruitmentBoardId), comment.parentComment.isNull())
                .orderBy(comment.createdAt.asc())
                .fetch();
    }

    @Override
    public Page<Comment> findPageByUser_idOrderByCreatedAtDsc(Long userId, Pageable pageable, EntireBoardType entireBoardType) {
        if (entireBoardType == EntireBoardType.SEMINAR_NOTICE || entireBoardType == EntireBoardType.SEMINAR_SUMMARY) {
            return findSeminarCommentPage(pageable, userId, entireBoardType);
        } else {
            return findRecruitmentCommentPage(pageable, userId, entireBoardType);
        }
    }

    public Page<Comment> findSeminarCommentPage(Pageable pageable, Long userId, EntireBoardType entireBoardType) {
        List<Comment> content = jpaQueryFactory
                .selectFrom(comment)
                .join(comment.board, board).fetchJoin()
                .where(comment.board.isNotNull(),
                        // TODO : Board 엔티티의 tag(type) enum이 잘 가공된 후에 boardType과 일치 조건 추가해야함
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
                        // TODO : Board 엔티티의 tag(type) enum이 잘 가공된 후에 boardType과 일치 조건 추가해야함
                        comment.user.id.eq(userId),
                        comment.deletedAt.isNull())
                .fetchOne();

        return new PageImpl<>(content, pageable, totalCount);
    }

    public Page<Comment> findRecruitmentCommentPage(Pageable pageable, Long userId, EntireBoardType entireBoardType) {
        List<Comment> content = jpaQueryFactory
                .selectFrom(comment)
                .join(comment.recruitmentBoard, recruitmentBoard).fetchJoin()
                .where(comment.recruitmentBoard.isNotNull(),
                        comment.recruitmentBoard.type.eq(RecruitmentBoardType.valueOf(entireBoardType.toString())),
                        comment.user.id.eq(userId),
                        comment.deletedAt.isNull())
                .orderBy(comment.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long totalCount = jpaQueryFactory
                .select(comment.count())
                .from(comment)
                .where(comment.recruitmentBoard.isNotNull(),
                        comment.recruitmentBoard.type.eq(RecruitmentBoardType.valueOf(entireBoardType.toString())),
                        comment.user.id.eq(userId),
                        comment.deletedAt.isNull())
                .fetchOne();

        return new PageImpl<>(content, pageable, totalCount);
    }
}
