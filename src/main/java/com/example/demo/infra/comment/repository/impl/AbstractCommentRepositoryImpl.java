package com.example.demo.infra.comment.repository.impl;

import com.example.demo.domain.comment.entity.CommentInfo;
import com.example.demo.domain.comment.entity.CommentUserInfo;
import com.example.demo.domain.comment.entity.MyCommentInfo;
import com.example.demo.domain.comment.repository.CommentRepository;
import com.example.demo.domain.recruitment_board.domain.entity.CommentBoard;
import com.example.demo.domain.recruitment_board.repository.CommentBoardJpaRepository;
import com.example.demo.infra.comment.repository.jpa.CommentJpaRepository;
import com.example.demo.infra.user.entity.User;
import com.example.demo.infra.user.repository.UserJpaRepository;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.infra.comment.entity.Comment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public abstract class AbstractCommentRepositoryImpl<T> implements CommentRepository<T> {
    protected final CommentJpaRepository<T> commentJpaRepository;
    protected final UserJpaRepository userJpaRepository;
    protected final CommentBoardJpaRepository commentBoardJpaRepository;

    protected final Class<? extends CommentBoard> boardClass;
    protected final Class<? extends Comment> commentClass;

    @PersistenceContext
    protected EntityManager entityManager;

    @Override
    public Optional<CommentInfo> getById(Long id) {
        return commentJpaRepository
                .doFindById(id)
                .map(Comment::toCommentInfoDomain);
    }

    @Override
    public Optional<CommentInfo> getByIdAndBoardId(Long id, Long boardId) {
        return commentJpaRepository
                .findByIdAndBoardId(id, boardId)
                .stream()
                .map(Comment::toCommentInfoDomain)
                .findFirst();
    }

    @Override
    public List<CommentInfo> getListByBoardId(Long boardId) {
        return commentJpaRepository
                .findListByBoardId(boardId)
                .stream()
                .map(Comment::toCommentInfoDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Page<MyCommentInfo> getPageByBoardId(Long userId, Pageable pageable, T boardType) {
        return commentJpaRepository.findPageByUserId(userId, pageable, boardType)
                .map(Comment::toMyCommentInfoDomain);
    }

    @Override
    public List<CommentUserInfo> getUsersByBoardIdAndParentCommentId(Long boardId, Long parentCommentId) {
        return commentJpaRepository.findUsersByBoardIdByParentCommentId(boardId, parentCommentId)
                .stream()
                .map(CommentUserInfo::from)
                .collect(Collectors.toList());
    }

    @Override
    public CommentInfo post(CommentInfo commentInfo) {
        User commentUser = findUserById(commentInfo.getCommentUserInfo().getUserId());
        CommentBoard commentBoard = findBoardById(commentInfo.getBoardId());

        Comment parentComment = null;
        if (commentInfo.getGroupId() != null) {
            parentComment = findCommentById(commentInfo.getGroupId());
        }

        return commentJpaRepository.doSave(commentInfo, commentUser, commentBoard, parentComment).toCommentInfoDomain();
    }

    @Override
    public void delete(CommentInfo commentInfo) {
        commentJpaRepository.doDelete(findCommentById(commentInfo.getCommentId()));
    }

    @Override
    public Optional<CommentInfo> patch(CommentInfo originComment, CommentInfo newComment) {
        Comment origin = findCommentById(originComment.getCommentId());
        origin.changeContent(newComment.getContent());

        return Optional.of(origin.toCommentInfoDomain());
    }


    protected <K> K findById(Class<K> entityClass, Long id) {
        return entityManager.find(entityClass, id);
    }

    protected User findUserById(Long userId) {
        User user = findById(User.class, userId);
        return user != null ? user : userJpaRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
    }

    protected CommentBoard findBoardById(Long boardId) {
        CommentBoard board = findById(boardClass, boardId);
        return board != null ? board : commentBoardJpaRepository.doFindById(boardId)
                .orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));
    }

    protected Comment findCommentById(Long commentId) {
        Comment comment = findById(commentClass, commentId);
        return comment != null ? comment : commentJpaRepository.doFindById(commentId)
                .orElseThrow(() -> new ServiceException(ErrorCode.COMMENT_NOT_FOUND));
    }
}
