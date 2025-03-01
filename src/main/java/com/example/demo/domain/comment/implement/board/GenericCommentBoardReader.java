package com.example.demo.domain.comment.implement.board;

public interface GenericCommentBoardReader {
    Long getById(Long id);

    Long getByIdWithUser(Long id);
}
