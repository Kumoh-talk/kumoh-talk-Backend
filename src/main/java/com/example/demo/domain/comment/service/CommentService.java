package com.example.demo.domain.comment.service;


import com.example.demo.domain.board.Repository.BoardRepository;
import com.example.demo.domain.board.domain.entity.Board;
import com.example.demo.domain.comment.domain.entity.Comment;
import com.example.demo.domain.comment.domain.request.CommentRequest;
import com.example.demo.domain.comment.domain.response.CommentInfoResponse;
import com.example.demo.domain.comment.domain.response.CommentPageResponse;
import com.example.demo.domain.comment.domain.response.CommentResponse;
import com.example.demo.domain.comment.repository.CommentRepository;
import com.example.demo.domain.study_project_board.domain.dto.vo.BoardType;
import com.example.demo.domain.study_project_board.domain.entity.StudyProjectBoard;
import com.example.demo.domain.study_project_board.repository.StudyProjectBoardRepository;
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
    private final StudyProjectBoardRepository studyProjectBoardRepository;

    @Transactional(readOnly = true)
    public CommentResponse findCommentsByBoardId(Long boardId, BoardType boardType) {
        List<Comment> commentList = findBoardComments(boardId, boardType);

        return CommentResponse.from(commentList);
    }

    @Transactional(readOnly = true)
    public CommentPageResponse findCommentsByUserId(Long userId, BoardType boardType, Pageable pageable) {
        userService.validateUser(userId);
        Page<Comment> commentPage = commentRepository.findCommentByUser_idOrderByCreatedAtDsc(pageable, userId, boardType);
        return CommentPageResponse.from(commentPage, boardType);
    }

    @Transactional
    public CommentInfoResponse saveComment(CommentRequest commentRequest, Long userId, Long boardId, BoardType boardType) {
        User commentUser = userService.validateUser(userId);

        Comment requestComment = requestToEntity(commentRequest, boardId, boardType, commentUser);

        Comment saved = commentRepository.save(requestComment);

        return CommentInfoResponse.from(saved);
    }

    @Transactional
    public CommentInfoResponse updateComment(CommentRequest commentRequest,
                                             Long commentId,
                                             Long userId) {
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
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findNotDeleteCommentById(commentId).orElseThrow(() ->
                new ServiceException(ErrorCode.COMMENT_NOT_FOUND)
        );

        if (userId.equals(comment.getUser().getId())) {
            commentRepository.replyCommentsDeleteById(commentId);
            commentRepository.delete(comment);
        } else {
            throw new ServiceException(ErrorCode.ACCESS_DENIED);
        }
    }

    public List<Comment> findBoardComments(Long boardId, BoardType boardType) {
        switch (boardType) {
            case SEMINAR_NOTICE, SEMINAR_SUMMARY -> {
                boardRepository.findById(boardId).orElseThrow(() ->
                        new ServiceException(ErrorCode.BOARD_NOT_FOUND)
                );
                return commentRepository.findByBoard_idOrderByCreatedAtAsc(boardId);
            }
            default -> {
                studyProjectBoardRepository.findById(boardId).orElseThrow(() ->
                        new ServiceException(ErrorCode.BOARD_NOT_FOUND)
                );
                return commentRepository.findByStudyProjectBoard_idOrderByCreatedAtAsc(boardId);
            }
        }
    }

    public Comment requestToEntity(CommentRequest commentRequest, Long boardId, BoardType boardType, User commentUser) {
        Comment requestComment = null;
        switch (boardType) {
            case SEMINAR_NOTICE, SEMINAR_SUMMARY -> {
                Board commentBoard = boardRepository.findById(boardId).orElseThrow(() ->
                        new ServiceException(ErrorCode.BOARD_NOT_FOUND)
                );

                Comment parentComment = null;
                if (commentRequest.getGroupId() != null) {
                    parentComment = commentRepository.findByIdAndBoard_Id(commentRequest.getGroupId(), boardId).orElseThrow(() ->
                            new ServiceException(ErrorCode.PARENT_NOT_FOUND));
                }
                requestComment = Comment.fromSeminarBoardRequest(commentRequest, commentBoard, commentUser, parentComment);
            }
            case STUDY, PROJECT -> {
                StudyProjectBoard studyProjectBoard = studyProjectBoardRepository.findById(boardId).orElseThrow(() ->
                        new ServiceException(ErrorCode.BOARD_NOT_FOUND)
                );

                Comment parentComment = null;
                if (commentRequest.getGroupId() != null) {
                    parentComment = commentRepository.findByIdAndStudyProjectBoard_Id(commentRequest.getGroupId(), boardId).orElseThrow(() ->
                            new ServiceException(ErrorCode.PARENT_NOT_FOUND));
                }
                requestComment = Comment.fromStudyProjectBoardRequest(commentRequest, studyProjectBoard, commentUser, parentComment);
            }
        }
        return requestComment;
    }
}
