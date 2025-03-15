package com.example.demo.infra.recruitment_board.repository.jpa;

import com.example.demo.infra.recruitment_board.entity.CommentBoard;

import java.util.Optional;

public interface CommentBoardJpaRepository {
    Optional<CommentBoard> doFindById(Long id);

    Optional<CommentBoard> findByIdWithUser(Long id);
}
