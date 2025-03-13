package com.example.demo.domain.recruitment_board.repository;

import com.example.demo.domain.recruitment_board.domain.entity.CommentBoard;

import java.util.Optional;

public interface CommentBoardJpaRepository {
    Optional<CommentBoard> doFindById(Long id);

    Optional<CommentBoard> findByIdWithUser(Long id);
}
