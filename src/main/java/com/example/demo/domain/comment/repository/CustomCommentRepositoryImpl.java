package com.example.demo.domain.comment.repository;

import static com.example.demo.domain.board.domain.entity.QBoard.board;
import static com.example.demo.domain.comment.domain.entity.QComment.comment;
import static com.example.demo.domain.user.domain.QUser.user;
import com.example.demo.domain.comment.domain.entity.Comment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
public class CustomCommentRepositoryImpl implements CustomCommentRepository{
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<Comment> findByBoard_idOrderByCreatedAtAsc(Long boardId) {
        return jpaQueryFactory
                .selectFrom(comment)
                .join(comment.board, board).where(board.id.eq(boardId), comment.parentComment.isNull())
                .join(comment.user, user).fetchJoin()
                .leftJoin(comment.replyComments).fetchJoin()
                .orderBy(comment.createdAt.asc())
                .fetch();
    }
}
