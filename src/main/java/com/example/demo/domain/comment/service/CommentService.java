package com.example.demo.domain.comment.service;


import com.example.demo.domain.board.Repository.BoardRepository;
import com.example.demo.domain.comment.domain.entity.Comment;
import com.example.demo.domain.comment.domain.request.CommentRequest;
import com.example.demo.domain.comment.domain.response.CommentInfo;
import com.example.demo.domain.comment.domain.response.CommentResponse;
import com.example.demo.domain.comment.repository.CommentRepository;
import com.example.demo.domain.board.domain.entity.Board;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private CommentRepository commentRepository;
    private BoardRepository boardRepository;
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public CommentResponse findByBoardId(Long boardId) {
        boardRepository.findPostByIdWithComments(boardId)
            .orElseThrow(() ->
                    new ServiceException(ErrorCode.BOARD_NOT_FOUND)
            );
        List<Comment> comments = commentRepository.findByBoard_idOrderByCreatedAtAsc(boardId);

        return CommentResponse.from(comments.stream()
                .map(CommentInfo::from)
                .collect(Collectors.toList()));
    }

    @Transactional
    public CommentInfo save(Long userId, CommentRequest commentRequest, Long boardId) {
        Board findBoard = boardRepository.findById(boardId).orElseThrow(() ->
                new ServiceException(ErrorCode.BOARD_NOT_FOUND));
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(ErrorCode.FAIL_USER_LOGIN));
        Comment parentComment;
        if (commentRequest.getGroupId() != null){
            parentComment = commentRepository.findById(commentRequest.getGroupId()).orElseThrow(() ->
                    new ServiceException(ErrorCode.PARENT_NOT_FOUND));
        } else{
            parentComment = null;
        }

        Comment requestComment = new Comment(commentRequest.getContents(), findBoard, findUser, parentComment);
        Comment saved = commentRepository.save(requestComment);

        return CommentInfo.from(saved);
    }

    @Transactional
    public CommentInfo update(CommentRequest commentRequest,
                              Long commentId,
                              String username) {
        Comment findComment = commentRepository.findById(commentId).orElseThrow(() ->
                new ServiceException(ErrorCode.COMMENT_NOT_FOUND)
        );
        findComment.changeContent(commentRequest.getContents());
        return CommentInfo.from(findComment);
    }
    @Transactional
    public void delete(Long commentId) {
        Comment findComment = commentRepository.findById(commentId).orElseThrow(() ->
                new ServiceException(ErrorCode.COMMENT_NOT_FOUND)
        );
        commentRepository.delete(findComment);
    }
}
