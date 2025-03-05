package com.example.demo.infra.comment.repository.impl;

import com.example.demo.domain.comment.entity.CommentInfo;
import com.example.demo.domain.comment.entity.CommentUserInfo;
import com.example.demo.domain.comment.entity.MyCommentInfo;
import com.example.demo.domain.comment.repository.CommentRepository;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.repository.UserJpaRepository;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.infra.base.EntityFinder;
import com.example.demo.infra.comment.entity.Comment;
import com.example.demo.infra.comment.repository.jpa.CommentJpaRepository;
import com.example.demo.infra.recruitment_board.entity.CommentBoard;
import com.example.demo.infra.recruitment_board.repository.jpa.CommentBoardJpaRepository;
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

    protected final EntityFinder entityFinder;

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
        User commentUser = entityFinder.findUserById(commentInfo.getCommentUserInfo().getUserId());
        CommentBoard commentBoard = findCommentBoardById(commentInfo.getBoardId());

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

    protected CommentBoard findCommentBoardById(Long boardId) {
        CommentBoard board = entityFinder.findById(boardClass, boardId);
        return board != null ? board : commentBoardJpaRepository.doFindById(boardId)
                .orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));
    }

    protected Comment findCommentById(Long commentId) {
        Comment comment = entityFinder.findById(commentClass, commentId);
        return comment != null ? comment : commentJpaRepository.doFindById(commentId)
                .orElseThrow(() -> new ServiceException(ErrorCode.COMMENT_NOT_FOUND));
    }
}
