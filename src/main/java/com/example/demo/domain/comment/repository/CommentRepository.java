package com.example.demo.domain.comment.repository;

import com.example.demo.domain.comment.domain.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByBoard_IdOrderByParentComment_IdAsc(Long boardId);

}
