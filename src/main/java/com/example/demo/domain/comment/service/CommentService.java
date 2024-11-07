package com.example.demo.domain.comment.service;


import com.example.demo.domain.board.Repository.BoardRepository;
import com.example.demo.domain.board.domain.entity.Board;
import com.example.demo.domain.comment.domain.dto.request.CommentRequest;
import com.example.demo.domain.comment.domain.dto.response.CommentInfoResponse;
import com.example.demo.domain.comment.domain.dto.response.CommentPageResponse;
import com.example.demo.domain.comment.domain.dto.response.CommentResponse;
import com.example.demo.domain.comment.domain.entity.Comment;
import com.example.demo.domain.comment.domain.vo.CommentTargetBoardType;
import com.example.demo.domain.comment.repository.CommentRepository;
import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentBoard;
import com.example.demo.domain.recruitment_board.domain.vo.BoardType;
import com.example.demo.domain.recruitment_board.repository.RecruitmentBoardRepository;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final UserService userService;

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final RecruitmentBoardRepository recruitmentBoardRepository;

    @Transactional(readOnly = true)
    public CommentResponse findCommentsByBoardId(Long boardId, CommentTargetBoardType boardType) {
        List<Comment> commentList = findCommentsFactory(boardId, boardType);

        return CommentResponse.from(commentList);
    }

    @Transactional(readOnly = true)
    public CommentPageResponse findCommentsByUserId(Long userId, Pageable pageable, BoardType boardType) {
        userService.validateUser(userId);

        Page<Comment> commentPage = commentRepository.findPageByUser_idOrderByCreatedAtDsc(userId, pageable, boardType);
        return CommentPageResponse.from(commentPage, boardType);
    }

    @Transactional
    public CommentInfoResponse saveComment(Long userId, Long boardId, CommentTargetBoardType boardType, CommentRequest commentRequest) {
        User commentUser = userService.validateUser(userId);

        Comment requestComment = transformToEntityFactory(commentUser, boardId, boardType, commentRequest);
        Comment saved = commentRepository.save(requestComment);

        return CommentInfoResponse.from(saved);
    }

    @Transactional
    public CommentInfoResponse updateComment(Long userId, Long commentId, CommentRequest commentRequest) {
        Comment comment = commentRepository.findNotDeleteCommentById(commentId).orElseThrow(() ->
                new ServiceException(ErrorCode.COMMENT_NOT_FOUND)
        );

        if (userId.equals(comment.getUser().getId())) {
            comment.changeContent(commentRequest.getContent());
        } else {
            throw new ServiceException(ErrorCode.ACCESS_DENIED);
        }

        return CommentInfoResponse.from(comment);
    }

    @Transactional
    public void deleteComment(Long userId, Long commentId, boolean isAuthorized) {
        Comment comment = commentRepository.findNotDeleteCommentById(commentId).orElseThrow(() ->
                new ServiceException(ErrorCode.COMMENT_NOT_FOUND));
        if (!isAuthorized) {
            if (!comment.getUser().getId().equals(userId)) {
                throw new ServiceException(ErrorCode.ACCESS_DENIED);
            }
        }

        commentRepository.replyCommentsDeleteById(commentId);
        commentRepository.delete(comment);
    }

    public List<Comment> findCommentsFactory(Long boardId, CommentTargetBoardType boardType) {
        switch (boardType) {
            case BASIC -> {
                boardRepository.findById(boardId).orElseThrow(() ->
                        new ServiceException(ErrorCode.BOARD_NOT_FOUND)
                );
                return commentRepository.findByBoard_idOrderByCreatedAtAsc(boardId);
            }
            default -> {
                recruitmentBoardRepository.findById(boardId).orElseThrow(() ->
                        new ServiceException(ErrorCode.BOARD_NOT_FOUND)
                );
                return commentRepository.findByRecruitmentBoard_idOrderByCreatedAtAsc(boardId);
            }
        }
    }

    public Comment transformToEntityFactory(User commentUser, Long boardId, CommentTargetBoardType boardType, CommentRequest commentRequest) {
        Comment parentComment = null;
        if (commentRequest.getGroupId() != null) {
            parentComment = commentRepository.findNotDeleteCommentById(commentRequest.getGroupId()).orElseThrow(() ->
                    new ServiceException(ErrorCode.PARENT_NOT_FOUND));
        }

        Comment requestComment = null;
        switch (boardType) {
            case BASIC -> {
                Board commentBoard = boardRepository.findById(boardId).orElseThrow(() ->
                        new ServiceException(ErrorCode.BOARD_NOT_FOUND)
                );

                requestComment = Comment.fromSeminarBoardRequest(commentUser, commentBoard, commentRequest, parentComment);
            }
            case RECRUITMENT -> {
                RecruitmentBoard recruitmentBoard = recruitmentBoardRepository.findById(boardId).orElseThrow(() ->
                        new ServiceException(ErrorCode.BOARD_NOT_FOUND)
                );
                requestComment = Comment.fromRecruitmentBoardRequest(commentUser, recruitmentBoard, commentRequest, parentComment);
            }
        }
        return requestComment;
    }
}
