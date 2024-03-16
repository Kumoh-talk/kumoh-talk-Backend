package com.example.demo.domain.post.service;

import com.example.demo.domain.file.FileUploader;
import com.example.demo.domain.file.domain.FileNameInfo;
import com.example.demo.domain.file.domain.entity.UploadFile;
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

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FileUploader fileUploader;

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

        FileNameInfo attachfileNameInfo = fileUploader.storeFile(postRequest.getAttachFile(), savedPost);
        List<FileNameInfo> imagesFileNameInfos = fileUploader.storePostFiles(postRequest.getImageFiles(),savedPost);

        return PostInfoResponse.from(
                savedPost,
                user.getName(),
                attachfileNameInfo,
                imagesFileNameInfos);
    }


    /**
     *
     * @param postRequest
     * @param userName
     * @param postId
     * @return
     */
    @Transactional
    public PostInfoResponse postUpdate(PostRequest postRequest, String userName, Long postId) throws IOException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다"));

        return updatePost(postRequest, post, userName);
    }


    /**
     * 게시물 삭제 메서드
     * @param postId
     */
    @Transactional
    public void postRemove(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다"));
        fileUploader.deletePostFiles(post);
        postRepository.delete(post);
    }
    /**
     * 게시물 id로 게시물을 찾는 메서드
     * @param postId
     * @param userName
     * @return
     */
    @Transactional(readOnly = true)
    public PostInfoResponse findById(Long postId,String userName) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다"));
        List<UploadFile> uploadFiles = post.getUploadFiles();
        return PostInfoResponse.from(post,
                userName,
                fileUploader.getPostAttachFile(uploadFiles),
                fileUploader.getPostImagesFiles(uploadFiles));
    }

    /**
     * 전체 게시물을 불러오는 메서드
     * @return List<PostInfoResponse>
     */
    @Transactional(readOnly = true)
    public List<PostInfoResponse> findByALL() {
/*        return postRepository.findAll().stream()
                .map(post -> PostInfoResponse.from(post, post.getUser().getName()))
                .collect(Collectors.toList());*/
        return null;  // 추후 pagging 처리 추가
    }



    public PostInfoResponse updatePost(PostRequest postRequest , Post post, String userName) throws IOException {
        fileUploader.deletePostFiles(post);
        FileNameInfo attachfileNameInfo = fileUploader.storeFile(postRequest.getAttachFile(), post);
        List<FileNameInfo> imagesFileNameInfos = fileUploader.storePostFiles(postRequest.getImageFiles(),post);
        post.setTitle(postRequest.getTitle());
        post.setContents(postRequest.getContents());

        return PostInfoResponse.from(post, userName, attachfileNameInfo, imagesFileNameInfos);
    }





}
