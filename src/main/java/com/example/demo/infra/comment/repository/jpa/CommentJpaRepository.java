package com.example.demo.infra.comment.repository.jpa;

import com.example.demo.domain.comment.entity.CommentInfo;
import com.example.demo.domain.recruitment_board.domain.entity.CommentBoard;
import com.example.demo.domain.user.domain.User;
import com.example.demo.infra.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CommentJpaRepository<T> {
    Optional<Comment> doFindById(Long id);

    Comment doSave(CommentInfo commentInfo, User commentUser, CommentBoard commentBoard, Comment parentComment);

    void doDelete(Comment comment);

    List<Comment> findByIdAndBoardId(Long id, Long boardId);

    List<Comment> findListByBoardId(Long boardId);

    Page<Comment> findPageByUserId(Long userId, Pageable pageable, T boardType);

    Optional<Comment> findNotDeleteCommentById(Long boardId, Long commentId);

    List<User> findUsersByBoardIdByParentCommentId(Long boardId, Long parentCommentId);
}
