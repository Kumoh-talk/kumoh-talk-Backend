package com.example.demo.domain.comment.service;


import com.example.demo.domain.board.Repository.BoardRepository;
import com.example.demo.domain.comment.domain.entity.Comment;
import com.example.demo.domain.comment.domain.request.CommentRequest;
import com.example.demo.domain.comment.domain.response.CommentInfoResponse;
import com.example.demo.domain.comment.repository.CommentRepository;
import com.example.demo.domain.board.domain.entity.Board;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.repository.UserRepository;
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
                new IllegalArgumentException("해당 id 의 게시물을 찾을 수 없습니다."));
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다"));

        Comment parentComment;
        if (commentRequest.getParentId() == null)
            parentComment = null;
        else
            parentComment = commentRepository.findById(commentRequest.getParentId()).orElseThrow(() ->
                    new IllegalArgumentException("부모 댓글을 찾을 수 없습니다."));

        Comment saved = commentRepository.save(new Comment(commentRequest.getContents(), findBoard, findUser, parentComment));
        return CommentInfoResponse.of(saved, findUser.getName());
    }

    @Transactional
    public CommentInfoResponse update(CommentRequest commentRequest,
                                      Long commentId,
                                      String username) {
        Comment findComment = commentRepository.findById(commentId).orElseThrow(() ->
                new IllegalArgumentException("해당 id 의 comment를 찾을 수 없습니다.")
        );
        findComment.changeContent(commentRequest.getContents());
        return CommentInfoResponse.of(findComment,username);
    }
    @Transactional
    public void delete(Long commentId) {
        Comment findComment = commentRepository.findById(commentId).orElseThrow(() ->
                new IllegalArgumentException("해당 id 의 comment를 찾을 수 없습니다.")
        );
        commentRepository.delete(findComment);
    }
    @Transactional(readOnly = true)
    public List<CommentInfoResponse> findByPostId(Long postId) {
        Board board = boardRepository.findPostByIdWithComments(postId)
                .orElseThrow(() ->
                        new IllegalArgumentException("해당 id 의 게시물을 찾을 수 없습니다.")
                );
        return board.getComments().stream()
                .map(comment -> Comment.newCommentInfoResponse(comment))
                .collect(Collectors.toList());
    }
}
