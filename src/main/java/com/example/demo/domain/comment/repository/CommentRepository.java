package com.example.demo.domain.comment.repository;

import com.example.demo.domain.comment.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>, CustomCommentRepository {

    @Modifying
    @Query("DELETE FROM Comment c WHERE c.parentComment.id = :commentId")
    void replyCommentsDeleteById(Long commentId);

    @Query("SELECT c FROM Comment c WHERE c.deletedAt is null and c.id = :id")
    Optional<Comment> findNotDeleteCommentById(Long id);
}