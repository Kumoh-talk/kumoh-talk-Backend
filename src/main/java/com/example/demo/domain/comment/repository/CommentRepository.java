package com.example.demo.domain.comment.repository;

import com.example.demo.domain.comment.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long>, CustomCommentRepository {

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Comment c WHERE c.parentComment.id = :commentId")
    void replyCommentsDeleteById(Long commentId);

}