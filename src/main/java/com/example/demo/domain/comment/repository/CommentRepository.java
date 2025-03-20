package com.example.demo.domain.comment.repository;

import com.example.demo.domain.comment.entity.CommentInfo;
import com.example.demo.domain.comment.entity.CommentUserInfo;
import com.example.demo.domain.comment.entity.MyCommentInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CommentRepository<T> {
    Optional<CommentInfo> getById(Long id);

    Optional<CommentInfo> getByIdAndBoardId(Long id, Long boardId);

    List<CommentInfo> getListByBoardId(Long boardId);

    Page<MyCommentInfo> getPageByBoardId(Long userId, Pageable pageable, T boardType);

    List<CommentUserInfo> getUsersByBoardIdAndParentCommentId(Long boardId, Long parentCommentId);

    Long getCommentCount(Long boardId);

    CommentInfo post(CommentInfo commentInfo);

    void delete(CommentInfo commentInfo);

    Optional<CommentInfo> patch(CommentInfo originComment, CommentInfo newComment);

}
