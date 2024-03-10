package com.example.demo.domain.post.service;

import com.example.demo.domain.post.Repository.PostRepository;
import com.example.demo.domain.post.domain.Post;
import com.example.demo.domain.post.domain.request.PostCreateRequest;
import com.example.demo.domain.post.domain.response.PostCreateResponse;
import com.example.demo.domain.post.domain.response.PostUpdateResponse;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    @Transactional
    public PostCreateResponse postSave(PostCreateRequest postCreateRequest, Long userId) {
        User user = userRepository.findById(userId).get();

        Post post = PostCreateRequest.toEntity(postCreateRequest, user);

        Post saved = postRepository.save(post);
        return PostCreateResponse.from(saved,user.getName());
    }
//    @Transactional
//    public PostUpdateResponse postUpdate(PostCreateRequest postCreateRequest, Long userId) {
//        User user = userRepository.findById(userId).get();
//
//        Post post = PostCreateRequest.toEntity(postCreateRequest, user);
//
//        Post saved = postRepository.save(post);
//        return PostCreateResponse.from(saved,user.getName());
//    }


}
