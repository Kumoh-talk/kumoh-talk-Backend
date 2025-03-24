package com.example.demo.infra.comment.repository.jpa;

import com.example.demo.domain.comment.entity.CommentInfo;
import com.example.demo.infra.comment.entity.Comment;
import com.example.demo.infra.recruitment_board.entity.CommentBoard;
import com.example.demo.infra.user.entity.User;
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

    Long countActiveComments(Long boardId);

    List<User> findUsersByBoardIdByParentCommentId(Long boardId, Long parentCommentId);
}
