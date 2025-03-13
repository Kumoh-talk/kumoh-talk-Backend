package com.example.demo.domain.comment.repository;

import com.example.demo.domain.comment.domain.dto.request.CommentRequest;
import com.example.demo.domain.comment.domain.dto.response.MyCommentResponse;
import com.example.demo.domain.comment.domain.entity.Comment;
import com.example.demo.domain.recruitment_board.domain.entity.GenericBoard;
import com.example.demo.domain.recruitment_board.domain.vo.EntireBoardType;
import com.example.demo.infra.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CommonCommentRepository<T> {
    Optional<Comment> doFindById(Long id);

    Comment doSave(User commentUser, GenericBoard commentBoard, CommentRequest commentRequest, Comment parentComment);

    void doDelete(Comment comment);

    List<Comment> findListByBoard_id(Long boardId);

    Page<MyCommentResponse> convertAndFindPageByUserId(Long userId, Pageable pageable, EntireBoardType entireBoardType);

    Page<MyCommentResponse> findPageByUser_id(Long userId, Pageable pageable, T boardType);

    void softDeleteReplyCommentsById(Long commentId);

    Optional<Comment> findNotDeleteCommentById(Long boardId, Long commentId);

    List<User> findUsersByBoard_idByParentComment_id(Long boardId, Long parentCommentId);
}
