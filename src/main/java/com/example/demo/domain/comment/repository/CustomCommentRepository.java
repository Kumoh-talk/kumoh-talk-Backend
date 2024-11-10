package com.example.demo.domain.comment.repository;

import com.example.demo.domain.comment.domain.entity.Comment;
import com.example.demo.domain.recruitment_board.domain.vo.EntireBoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomCommentRepository {
    List<Comment> findByBoard_idOrderByCreatedAtAsc(Long boardId);

    List<Comment> findByRecruitmentBoard_idOrderByCreatedAtAsc(Long recruitmentBoardId);

    Page<Comment> findPageByUser_idOrderByCreatedAtDsc(Long userId, Pageable pageable, EntireBoardType entireBoardType);
}
