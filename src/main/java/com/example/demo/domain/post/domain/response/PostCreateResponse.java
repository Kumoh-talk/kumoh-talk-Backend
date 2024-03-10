package com.example.demo.domain.post.domain.response;

import com.example.demo.domain.post.domain.Post;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.dto.response.UserUpdateResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PostCreateResponse {
    private Long postId;
    private String username;
    private String title;
    private String contents;
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime updatedAt;


    public static PostCreateResponse from(Post post,String username) {
        return new PostCreateResponse(
                post.getId(),
                username,
                post.getTitle(),
                post.getContents(),
                post.getUpdatedAt()
        );
    }

}
