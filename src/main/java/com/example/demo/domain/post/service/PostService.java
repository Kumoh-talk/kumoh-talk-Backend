package com.example.demo.domain.post.service;

import com.example.demo.domain.file.FileStore;
import com.example.demo.domain.file.domain.FileNameInfo;
import com.example.demo.domain.file.domain.UploadFile;
import com.example.demo.domain.file.repository.FileRepository;
import com.example.demo.domain.post.Repository.PostRepository;
import com.example.demo.domain.post.domain.Post;
import com.example.demo.domain.post.domain.request.PostRequest;
import com.example.demo.domain.post.domain.response.PostInfoResponse;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FileStore fileStore;
    private final FileRepository fileRepository;

    /**
     *
     * @param postRequest
     * @param userId
     * @return PostInfoResponse 생성된 post 정보 객체 반환
     */
    @Transactional
    public PostInfoResponse postSave(PostRequest postRequest, Long userId) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다"));

        Post post = PostRequest.toEntity(postRequest, user);
        Post savedPost = postRepository.save(post);

        List<FileNameInfo> files = fileStore.storePostFiles(postRequest.getAttachFiles(),savedPost);

        return PostInfoResponse.from(savedPost,user.getName(),files);
    }


    /**
     *
     * @param postRequest
     * @param userName
     * @param postId
     * @return
     */
    @Transactional
    public PostInfoResponse postUpdate(PostRequest postRequest, String userName, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다"));
        return updatePost(post, postRequest, userName);
    }


    /**
     *
     * @param postId
     */
    @Transactional
    public void postRemove(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다"));

        postRepository.delete(post);

    }
    /**
     * @param postId
     * @param userName
     * @return
     */
    @Transactional(readOnly = true)
    public PostInfoResponse findById(Long postId,String userName) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다"));
        return PostInfoResponse.from(post, userName);
    }

    /**
     *
     * @return List<PostInfoResponse>
     */
    @Transactional(readOnly = true)
    public List<PostInfoResponse> findByALL() {
        return postRepository.findAll().stream()
                .map(post -> PostInfoResponse.from(post, post.getUser().getName()))
                .collect(Collectors.toList());
    }




    private PostInfoResponse updatePost(Post post, PostRequest postRequest, String userName) {
        post.setTitle(postRequest.getTitle());
        post.setContents(postRequest.getContents());
        return PostInfoResponse.from(post, userName);
    }




}
