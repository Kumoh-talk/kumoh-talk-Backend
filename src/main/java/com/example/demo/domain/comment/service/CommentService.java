package com.example.demo.domain.comment.service;


import com.example.demo.domain.post.Repository.PostRepository;
import com.example.demo.domain.comment.domain.Comment;
import com.example.demo.domain.comment.domain.request.CommentSaveRequest;
import com.example.demo.domain.comment.domain.request.CommentUpdateRequest;
import com.example.demo.domain.comment.domain.response.CommentInfoResponse;
import com.example.demo.domain.comment.repository.CommentRepository;
import com.example.demo.domain.post.domain.Post;
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
    private PostRepository postRepository;
    private UserRepository userRepository;
    @Transactional
    public CommentInfoResponse save(Long userId, CommentSaveRequest commentSaveRequest) {
        Post findPost = postRepository.findById(commentSaveRequest.getPostId()).orElseThrow(() ->
                new IllegalArgumentException("해당 id 의 게시물을 찾을 수 없습니다.")
        );
        User findUser = userRepository.findById(userId).get();
        Comment comment = new Comment(commentSaveRequest.getContents(), findPost, findUser);
        Comment saved = commentRepository.save(comment);
        return CommentInfoResponse.from(saved);
    }
    @Transactional
    public CommentInfoResponse update(CommentUpdateRequest commentUpdateRequest) {
        Comment findComment = commentRepository.findById(commentUpdateRequest.getCommentId()).orElseThrow(() ->
                new IllegalArgumentException("해당 id 의 comment를 찾을 수 없습니다.")
        );
        findComment.setContents(commentUpdateRequest.getContents());
        return CommentInfoResponse.from(findComment);
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
        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new IllegalArgumentException("해당 id 의 게시물을 찾을 수 없습니다.")
                );
        return post.getComments().stream()
                .map(comment -> Comment.entityToResponse(comment))
                .collect(Collectors.toList());
    }






}
