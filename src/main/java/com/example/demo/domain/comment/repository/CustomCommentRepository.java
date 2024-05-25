package com.example.demo.domain.comment.repository;

import com.example.demo.domain.comment.domain.entity.Comment;

import java.util.List;

public interface CustomCommentRepository {
    List<Comment> findByBoard_idOrderByCreatedAtAsc(Long boardId);
}
