package com.example.demo.infra.comment.repository.impl;

import com.example.demo.domain.comment.entity.CommentInfo;
import com.example.demo.domain.comment.entity.CommentUserInfo;
import com.example.demo.domain.comment.entity.MyCommentInfo;
import com.example.demo.domain.comment.repository.CommentRepository;
import com.example.demo.infra.comment.entity.Comment;
import com.example.demo.infra.comment.repository.jpa.CommentJpaRepository;
import com.example.demo.infra.recruitment_board.entity.CommentBoard;
import com.example.demo.infra.recruitment_board.repository.jpa.CommentBoardJpaRepository;
import com.example.demo.infra.user.entity.User;
import com.example.demo.infra.user.repository.UserJpaRepository;
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
    public Long getCommentCount(Long boardId) {
        return commentJpaRepository.countActiveComments(boardId);
    }

    @Override
    public CommentInfo post(CommentInfo commentInfo) {
        User commentUser = userJpaRepository.findById(commentInfo.getCommentUserInfo().getUserId()).get();
        CommentBoard commentBoard = commentBoardJpaRepository.doFindById(commentInfo.getBoardId()).get();

        Comment parentComment = null;
        if (commentInfo.getGroupId() != null) {
            parentComment = commentJpaRepository.doFindById(commentInfo.getGroupId()).get();
        }

        return commentJpaRepository.doSave(commentInfo, commentUser, commentBoard, parentComment).toCommentInfoDomain();
    }

    @Override
    public void delete(CommentInfo commentInfo) {
        commentJpaRepository.doDelete(commentJpaRepository.doFindById(commentInfo.getCommentId()).get());
    }

    @Override
    public Optional<CommentInfo> patch(CommentInfo originComment, CommentInfo newComment) {
        Comment origin = commentJpaRepository.doFindById(originComment.getCommentId()).get();
        origin.changeContent(newComment.getContent());

        return Optional.of(origin.toCommentInfoDomain());
    }
}
