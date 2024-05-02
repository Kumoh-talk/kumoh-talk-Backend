package com.example.demo.domain.comment.service;


import com.example.demo.domain.board.Repository.BoardRepository;
import com.example.demo.domain.comment.domain.entity.Comment;
import com.example.demo.domain.comment.domain.request.CommentRequest;
import com.example.demo.domain.comment.domain.response.CommentInfoResponse;
import com.example.demo.domain.comment.repository.CommentRepository;
import com.example.demo.domain.board.domain.entity.Board;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private CommentRepository commentRepository;
    private BoardRepository boardRepository;
    private UserRepository userRepository;

    @Transactional
    public CommentInfoResponse save(Long userId, CommentRequest commentRequest,Long boardId) {
        Board findBoard = boardRepository.findById(boardId).orElseThrow(() ->
                new ServiceException(ErrorCode.BOARD_NOT_FOUND));
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(ErrorCode.FAIL_USER_LOGIN));

        Comment parentComment;
        if (commentRequest.getParentId() == null)
            parentComment = null;
        else
            parentComment = commentRepository.findById(commentRequest.getParentId()).orElseThrow(() ->
                    new ServiceException(ErrorCode.PARENT_NOT_FOUND));

        Comment saved = commentRepository.save(new Comment(commentRequest.getContents(), findBoard, findUser, parentComment));
        return CommentInfoResponse.of(saved, findUser.getName());
    }

    @Transactional
    public CommentInfoResponse update(CommentRequest commentRequest,
                                      Long commentId,
                                      String username) {
        Comment findComment = commentRepository.findById(commentId).orElseThrow(() ->
                new ServiceException(ErrorCode.COMMENT_NOT_FOUND)
        );
        findComment.changeContent(commentRequest.getContents());
        return CommentInfoResponse.of(findComment,username);
    }
    @Transactional
    public void delete(Long commentId) {
        Comment findComment = commentRepository.findById(commentId).orElseThrow(() ->
                new ServiceException(ErrorCode.COMMENT_NOT_FOUND)
        );
        commentRepository.delete(findComment);
    }
    @Transactional(readOnly = true)
    public List<CommentInfoResponse> findByPostId(Long postId) {
        Board board = boardRepository.findPostByIdWithComments(postId)
                .orElseThrow(() ->
                        new ServiceException(ErrorCode.BOARD_NOT_FOUND)
                );

        // boar의 댓글을 Page<Comment> 객체로 형변환하여 페이징처리

        return board.getComments().stream()
                .map(comment -> Comment.newCommentInfoResponse(comment))
                .collect(Collectors.toList());
    }
}
