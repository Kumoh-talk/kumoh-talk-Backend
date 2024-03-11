package com.example.demo.domain.post.post_qna.service;

import com.example.demo.domain.post.Repository.PostRepository;
import com.example.demo.domain.post.domain.Post;
import com.example.demo.domain.post.post_qna.domain.Post_Qna;
import com.example.demo.domain.post.post_qna.domain.request.QnaSaveRequest;
import com.example.demo.domain.post.post_qna.domain.request.QnaUpdateRequest;
import com.example.demo.domain.post.post_qna.domain.response.QnaInfoResponse;
import com.example.demo.domain.post.post_qna.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QnaService {

    private QnaRepository qnaRepository;
    private PostRepository postRepository;

    @Transactional
    public QnaInfoResponse save(QnaSaveRequest qnaSaveRequest) {
        Post post = postRepository.findById(qnaSaveRequest.getPostId())
                .orElseThrow(() ->
                        new IllegalArgumentException("해당 id 의 게시물을 찾을 수 없습니다.")
                );
        Post_Qna entity = QnaSaveRequest.toEntity(qnaSaveRequest, post);
        return QnaInfoResponse.from(qnaRepository.save(entity));

    }

    @Transactional
    public QnaInfoResponse update(QnaUpdateRequest qnaUpdateRequest) {
        Post_Qna postQna = qnaRepository.findById(qnaUpdateRequest.getQnaId()).orElseThrow(() ->
                new IllegalArgumentException("해당 id 의 게시물을 찾을 수 없습니다.")
        );
        postQna.setTitle(qnaUpdateRequest.getTitle());
        postQna.setContents(qnaUpdateRequest.getContents());
        return QnaInfoResponse.from(postQna);
    }

    @Transactional
    public void delete(Long qnaId) {
        Post_Qna postQna = qnaRepository.findById(qnaId).orElseThrow(() ->
                new IllegalArgumentException("해당 id 의 게시물을 찾을 수 없습니다.")
        );
        qnaRepository.delete(postQna);
    }

    @Transactional(readOnly = true)
    public List<Post_Qna> findByPostId(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new IllegalArgumentException("해당 id 의 게시물을 찾을 수 없습니다.")
                );
        return post.getPostQnas();
    }



}
