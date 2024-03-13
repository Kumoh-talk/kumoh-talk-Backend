package com.example.demo.domain.post.domain.response;

import com.example.demo.domain.file.domain.FileNameInfo;
import com.example.demo.domain.post.domain.Post;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class PostInfoResponse {
    private Long postId;
    private String username;
    private String title;
    private String contents;
    private List<FileNameInfo> attachFileNameInfos;

    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime updatedAt;

    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime createdAt;
    public static PostInfoResponse from(Post post, String username,List<FileNameInfo> attachFileNameInfos) {
        return new PostInfoResponse(
                post.getId(),
                username,
                post.getTitle(),
                post.getContents(),
                attachFileNameInfos,
                post.getUpdatedAt(),
                post.getCreatedAt()
        );
    }

}
