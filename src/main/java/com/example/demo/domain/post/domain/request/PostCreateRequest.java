package com.example.demo.domain.post.domain.request;


import com.example.demo.domain.post.domain.Post;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.vo.Track;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Setter
@Getter
public class PostCreateRequest {

    @NotBlank(message = "제목은 필수 항목입니다.")
    @Max(value = 45,message = "최대 제한 45글자 입니다.")
    private String title;

    @NotBlank(message = "게시물 내용은 필수 항목입니다.")
    @Max(value = 45,message = "최대 제한 500글자 입니다.")
    private String contents;

    @NotBlank(message = "트랙을 지정해야합니다.")
    private Track track;

    public static Post toEntity(PostCreateRequest postCreateRequest, User user) {
        return Post.builder()
                .title(postCreateRequest.getTitle())
                .contents(postCreateRequest.getContents())
                .track(user.getTrack())
                .user(user) // 과제 게시판의 트랙은 글쓴이의 트랙으로 지정
                .build();

    }
}
