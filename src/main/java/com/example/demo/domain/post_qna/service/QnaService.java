package com.example.demo.domain.post_qna.service;

import com.example.demo.domain.post.Repository.PostRepository;
import com.example.demo.domain.post.domain.Post;
import com.example.demo.domain.post.post_qna.domain.Post_Qna;
import com.example.demo.domain.post.post_qna.domain.request.QnaRequest;
import com.example.demo.domain.post.post_qna.domain.response.QnaInfoResponse;
import com.example.demo.domain.post.post_qna.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QnaService {

    private QnaRepository qnaRepository;
    private PostRepository postRepository;

    @Transactional
    public QnaInfoResponse save(QnaRequest qnaRequest,Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new IllegalArgumentException("해당 id 의 게시물을 찾을 수 없습니다.")
                );
        Post_Qna entity = QnaRequest.toEntity(qnaRequest, post);
        return QnaInfoResponse.from(qnaRepository.save(entity));

    }

    @Transactional
    public QnaInfoResponse update(QnaRequest qnaRequest,Long qnaId) {
        Post_Qna postQna = qnaRepository.findById(qnaId)
                .orElseThrow(() -> new IllegalArgumentException("해당 id 의 게시물을 찾을 수 없습니다."));
        postQna.setTitle(qnaRequest.getTitle());
        postQna.setContents(qnaRequest.getContents());
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
    public List<QnaInfoResponse> findByPostId(Long postId) {
        Post post = postRepository.findByIdWithPostQnas(postId)
                .orElseThrow(() ->
                        new IllegalArgumentException("해당 id 의 게시물을 찾을 수 없습니다.")
                );
        return post.getPostQnas().stream()
                .map(postQna -> QnaInfoResponse.from(postQna))
                .collect(Collectors.toList());
    }


}
