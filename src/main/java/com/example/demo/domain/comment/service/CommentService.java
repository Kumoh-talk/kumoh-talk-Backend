package com.example.demo.domain.comment.service;


import com.example.demo.domain.board.Repository.BoardRepository;
import com.example.demo.domain.board.domain.entity.Board;
import com.example.demo.domain.comment.domain.entity.Comment;
import com.example.demo.domain.comment.domain.request.CommentRequest;
import com.example.demo.domain.comment.domain.response.CommentInfo;
import com.example.demo.domain.comment.domain.response.CommentPageResponse;
import com.example.demo.domain.comment.domain.response.CommentResponse;
import com.example.demo.domain.comment.repository.CommentRepository;
import com.example.demo.domain.study_project_board.domain.dto.vo.BoardCategory;
import com.example.demo.domain.study_project_board.domain.entity.StudyProjectBoard;
import com.example.demo.domain.study_project_board.repository.StudyProjectBoardRepository;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.repository.UserRepository;
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
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final StudyProjectBoardRepository studyProjectBoardRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public CommentResponse findCommentsByBoardId(Long boardId, BoardCategory boardCategory) {
        validateBoard(boardId, boardCategory);

        List<Comment> commentList = commentRepository.findByBoard_idOrderByCreatedAtAsc(boardId);

        return CommentResponse.from(commentList);
    }

    @Transactional(readOnly = true)
    public CommentPageResponse findCommentsByUserId(Long userId, BoardCategory boardCategory, Pageable pageable) {
        Page<Comment> commentPage = commentRepository.findCommentByUser_idOrderByCreatedAtDsc(pageable, userId, boardCategory);
        return CommentPageResponse.from(commentPage, boardCategory);
    }

    @Transactional
    public CommentInfo saveComment(CommentRequest commentRequest, Long userId, Long boardId, BoardCategory boardCategory) {
        User commentUser = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));

        Comment parentComment;
        if (commentRequest.getGroupId() != null) {
            parentComment = commentRepository.findById(commentRequest.getGroupId()).orElseThrow(() ->
                    new ServiceException(ErrorCode.PARENT_NOT_FOUND));
        } else {
            parentComment = null;
        }

        Board commentBoard;
        StudyProjectBoard studyProjectBoard;
        Comment requestComment = null;
        switch (boardCategory) {
            case SEMINAR -> {
                commentBoard = boardRepository.findById(boardId).orElseThrow(() ->
                        new ServiceException(ErrorCode.BOARD_NOT_FOUND)
                );
                requestComment = Comment.fromSeminarBoardRequest(commentRequest, commentBoard, commentUser, parentComment);
            }
            case STUDY, PROJECT -> {
                studyProjectBoard = studyProjectBoardRepository.findById(boardId).orElseThrow(() ->
                        new ServiceException(ErrorCode.BOARD_NOT_FOUND)
                );
                requestComment = Comment.fromStudyProjectBoardRequest(commentRequest, studyProjectBoard, commentUser, parentComment);
            }
        }

        Comment saved = commentRepository.save(requestComment);

        return CommentInfo.from(saved);
    }

    @Transactional
    public CommentInfo updateComment(CommentRequest commentRequest,
                                     Long commentId,
                                     Long userId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new ServiceException(ErrorCode.COMMENT_NOT_FOUND)
        );

        if (userId.equals(comment.getUser().getId())) {
            comment.changeContent(commentRequest.getContent());
        } else {
            throw new ServiceException(ErrorCode.ACCESS_DENIED);
        }

        return CommentInfo.from(comment);
    }

    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new ServiceException(ErrorCode.COMMENT_NOT_FOUND)
        );

        if (userId.equals(comment.getUser().getId())) {
            commentRepository.delete(comment);
            deleteReplyComments(comment);
        } else {
            throw new ServiceException(ErrorCode.ACCESS_DENIED);
        }
    }

    public void deleteReplyComments(Comment parentComment) {
        for (Comment replyComment : parentComment.getReplyComments()) {
            commentRepository.delete(replyComment);
        }
    }

    public void validateBoard(Long boardId, BoardCategory boardCategory) {
        switch (boardCategory) {
            case SEMINAR -> boardRepository.findById(boardId).orElseThrow(() ->
                    new ServiceException(ErrorCode.BOARD_NOT_FOUND)
            );
            case STUDY, PROJECT -> studyProjectBoardRepository.findById(boardId).orElseThrow(() ->
                    new ServiceException(ErrorCode.BOARD_NOT_FOUND)
            );
        }
    }
}
