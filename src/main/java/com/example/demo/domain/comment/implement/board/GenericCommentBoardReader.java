package com.example.demo.domain.comment.implement.board;

public interface GenericCommentBoardReader {
    boolean existsById(Long id);

    boolean existsByIdWithUser(Long id);
}
