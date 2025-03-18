
package com.example.demo.domain.comment.service;

import com.example.demo.domain.comment.entity.CommentInfo;
import com.example.demo.domain.comment.entity.MyCommentInfo;
import com.example.demo.domain.comment.implement.board.GenericCommentBoardReader;
import com.example.demo.domain.comment.implement.comment.AbstractCommentHandler;
import com.example.demo.domain.notification.domain.vo.NotificationType;
import com.example.demo.domain.recruitment_board.entity.vo.EntireBoardType;
import com.example.demo.domain.user.implement.UserReader;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
public abstract class AbstractCommentService {

    protected final UserReader userReader;
    protected final AbstractCommentHandler<?> commentHandler;
    protected final GenericCommentBoardReader commentBoardReader;
//    protected final CommentNotificationHandler commentNotificationHandler;

    @Transactional(readOnly = true)
    public List<CommentInfo> getCommentsByBoardId(Long boardId) {
        if (!commentBoardReader.existsById(boardId)) {
            throw new ServiceException(ErrorCode.BOARD_NOT_FOUND);
        }

        return commentHandler.getListByBoardId(boardId);
    }

    @Transactional(readOnly = true)
    public Page<MyCommentInfo> getCommentsByUserId(Long userId, Pageable pageable, EntireBoardType entireBoardType) {
        userReader.findUserTarget(userId).orElseThrow(() ->
                new ServiceException(ErrorCode.USER_NOT_FOUND));

        return commentHandler.getPageByBoardId(userId, pageable, entireBoardType);
    }

    @Transactional
    public CommentInfo postComment(CommentInfo commentInfo, NotificationType notificationType) {
        if (!commentBoardReader.existsByIdWithUser(commentInfo.getBoardId())) {
            throw new ServiceException(ErrorCode.BOARD_NOT_FOUND);
        }
        userReader.findUserTarget(commentInfo.getCommentUserInfo().getUserId())
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));

        if (commentInfo.getGroupId() != null) {
            commentHandler.getParentComment(commentInfo.getGroupId(), commentInfo.getBoardId())
                    .orElseThrow(() -> new ServiceException(ErrorCode.PARENT_NOT_FOUND));
        }

        CommentInfo saved = commentHandler.post(commentInfo);
//        commentNotificationHandler.saveCommentNotification(saved, notificationType, genericCommentHandler);
        return saved;
    }

    @Transactional
    public CommentInfo patchComment(CommentInfo commentInfo) {
        CommentInfo comment = commentHandler.getById(commentInfo.getCommentId()).orElseThrow(() ->
                new ServiceException(ErrorCode.COMMENT_NOT_FOUND)
        );

        return commentHandler.patch(comment, commentInfo)
                .orElseThrow(() -> new ServiceException(ErrorCode.ACCESS_DENIED));
    }

    @Transactional
    public void deleteComment(Long userId, Long commentId, boolean isAuthorized) {
        CommentInfo commentInfo = commentHandler.getById(commentId).orElseThrow(() ->
                new ServiceException(ErrorCode.COMMENT_NOT_FOUND));

        if (!isAuthorized) {
            if (!commentInfo.getCommentUserInfo().getUserId().equals(userId)) {
                throw new ServiceException(ErrorCode.ACCESS_DENIED);
            }
        }

        commentHandler.delete(commentInfo);
    }
}
