package com.example.demo.infra.comment.repository.impl;

import com.example.demo.domain.board.service.entity.vo.BoardType;
import com.example.demo.domain.user.repository.UserJpaRepository;
import com.example.demo.infra.base.EntityFinder;
import com.example.demo.infra.board.entity.Board;
import com.example.demo.infra.board.repository.BoardJpaRepository;
import com.example.demo.infra.comment.entity.BoardComment;
import com.example.demo.infra.comment.repository.jpa.BoardCommentJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class BoardCommentRepositoryImpl extends AbstractCommentRepositoryImpl<BoardType> {
    public BoardCommentRepositoryImpl(
            BoardCommentJpaRepository commentJpaRepository,
            UserJpaRepository userJpaRepository,
            BoardJpaRepository boardJpaRepository,
            EntityFinder entityFinder) {
        super(commentJpaRepository, userJpaRepository, boardJpaRepository, Board.class, BoardComment.class, entityFinder);
    }
}
