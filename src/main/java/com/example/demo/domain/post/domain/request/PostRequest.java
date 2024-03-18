package com.example.demo.domain.post.domain.request;


import com.example.demo.domain.post.domain.Post;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.vo.Track;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Setter
@Getter
public class PostRequest {

    @NotBlank(message = "제목은 필수 항목입니다.")
    @Size(max = 45,message = "최대 제한 45글자 입니다.")
    private String title;

    @NotBlank(message = "게시물 내용은 필수 항목입니다.")
    @Size(max = 500,message = "최대 제한 500글자 입니다.")
    private String contents;

    @NotNull(message = "트랙을 지정해야합니다.")
    private Track track;

    @Nullable
    private MultipartFile attachFile;

    @Nullable
    private List<MultipartFile> imageFiles;

    @Builder
    public PostRequest(String title, String contents, Track track, @Nullable MultipartFile attachFile, @Nullable List<MultipartFile> imageFiles) {
        this.title = title;
        this.contents = contents;
        this.track = track;
        this.attachFile = attachFile;
        this.imageFiles = imageFiles;
    }

    public static Post toEntity(PostRequest postRequest, User user) {
        return Post.builder()
                .title(postRequest.getTitle())
                .contents(postRequest.getContents())
                .track(user.getTrack())
                .user(user) // 과제 게시판의 트랙은 글쓴이의 트랙으로 지정
                .build();

    }
}
