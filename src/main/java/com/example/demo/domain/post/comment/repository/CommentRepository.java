package com.example.demo.domain.post.comment.repository;

import com.example.demo.domain.post.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {
}
