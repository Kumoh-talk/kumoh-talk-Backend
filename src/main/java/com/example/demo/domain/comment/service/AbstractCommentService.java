package com.example.demo.domain.comment.service;

import com.example.demo.domain.comment.domain.dto.request.CommentRequest;
import com.example.demo.domain.comment.domain.dto.response.CommentInfoResponse;
import com.example.demo.domain.comment.domain.dto.response.CommentResponse;
import com.example.demo.domain.comment.domain.dto.response.MyCommentResponse;
import com.example.demo.domain.comment.domain.entity.Comment;
import com.example.demo.domain.comment.repository.CommonCommentRepository;
import com.example.demo.domain.notification.domain.vo.NotificationType;
import com.example.demo.domain.recruitment_board.domain.entity.GenericBoard;
import com.example.demo.domain.recruitment_board.domain.vo.EntireBoardType;
import com.example.demo.domain.recruitment_board.repository.CommonBoardRepository;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.global.base.dto.page.GlobalPageResponse;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.infra.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public abstract class AbstractCommentService {

    protected final UserService userService;
    protected final CommentNotificationService commentNotificationService;

    protected final CommonCommentRepository<?> commentRepository;
    protected final CommonBoardRepository boardRepository;

    @Transactional(readOnly = true)
    public CommentResponse findCommentsByBoardId(Long boardId) {
        boardRepository.doFindById(boardId).orElseThrow(() ->
                new ServiceException(ErrorCode.BOARD_NOT_FOUND)
        );

        return CommentResponse.fromComment(commentRepository.findListByBoard_id(boardId));
    }

    @Transactional(readOnly = true)
    public GlobalPageResponse<MyCommentResponse> findCommentsByUserId(Long userId, Pageable pageable, EntireBoardType entireBoardType) {
        userService.validateUser(userId);
        return GlobalPageResponse.create(commentRepository.convertAndFindPageByUserId(userId, pageable, entireBoardType));
    }

    @Transactional
    public CommentInfoResponse saveComment(Long userId, Long boardId, CommentRequest commentRequest, NotificationType notificationType) {
        GenericBoard commentBoard = boardRepository.findByIdWithUser(boardId).orElseThrow(() ->
                new ServiceException(ErrorCode.BOARD_NOT_FOUND)
        );
        User commentUser = userService.validateUser(userId);
        Comment parentComment = null;
        if (commentRequest.getGroupId() != null) {
            parentComment = commentRepository.findNotDeleteCommentById(boardId, commentRequest.getGroupId()).orElseThrow(() ->
                    new ServiceException(ErrorCode.PARENT_NOT_FOUND));
        }

        Comment saved = commentRepository.doSave(commentUser, commentBoard, commentRequest, parentComment);
        commentNotificationService.saveCommentNotification(saved, notificationType, commentRepository);
        return CommentInfoResponse.fromComment(saved);
    }

    @Transactional
    public CommentInfoResponse updateComment(Long userId, Long commentId, CommentRequest commentRequest) {
        Comment comment = commentRepository.doFindById(commentId).orElseThrow(() ->
                new ServiceException(ErrorCode.COMMENT_NOT_FOUND)
        );

        if (userId.equals(comment.getUser().getId())) {
            comment.changeContent(commentRequest.getContent());
        } else {
            throw new ServiceException(ErrorCode.ACCESS_DENIED);
        }

        return CommentInfoResponse.fromComment(comment);
    }

    @Transactional
    public void deleteComment(Long userId, Long commentId, boolean isAuthorized) {
        Comment comment = commentRepository.doFindById(commentId).orElseThrow(() ->
                new ServiceException(ErrorCode.COMMENT_NOT_FOUND));

        if (!isAuthorized) {
            if (!comment.getUser().getId().equals(userId)) {
                throw new ServiceException(ErrorCode.ACCESS_DENIED);
            }
        }

        commentRepository.doDelete(comment);
    }
}
