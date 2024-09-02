package com.example.demo.domain.comment.repository;

import com.example.demo.domain.comment.domain.entity.Comment;
import com.example.demo.domain.study_project_board.domain.dto.vo.BoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomCommentRepository {
    List<Comment> findByBoard_idOrderByCreatedAtAsc(Long boardId);

    List<Comment> findByStudyProjectBoard_idOrderByCreatedAtAsc(Long boardId);

    Page<Comment> findCommentByUser_idOrderByCreatedAtDsc(Pageable pageable, Long userId, BoardType boardType);
}
