package com.example.demo.domain.post.post_qna.service;

import com.example.demo.domain.post.Repository.PostRepository;
import com.example.demo.domain.post.domain.Post;
import com.example.demo.domain.post.post_qna.domain.Post_Qna;
import com.example.demo.domain.post.post_qna.domain.request.QnaSaveRequest;
import com.example.demo.domain.post.post_qna.domain.response.QnaInfoResponse;
import com.example.demo.domain.post.post_qna.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QnaService {

    private QnaRepository qnaRepository;
    private PostRepository postRepository;

    @Transactional
    public QnaInfoResponse save(QnaSaveRequest qnaSaveRequest) {
        Post post = postRepository.findById(qnaSaveRequest.getPostId())
                .orElseThrow(() -> {
                    return new  IllegalArgumentException("해당 id 의 게시물을 찾을 수 없습니다.");
                });
        Post_Qna entity = QnaSaveRequest.toEntity(qnaSaveRequest, post);
        return QnaInfoResponse.from(qnaRepository.save(entity));

    }





}
