package com.example.demo.domain.post.comment.domain.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentSaveRequest {

    @NotBlank(message = " 게시글의 고유 id가 누락 되었습니다.")
    private Long postId;

    @NotBlank(message = "내용은 필수 항목입니다.")
    @Max(value = 500,message = "최대 제한 500글자 입니다.")
    private String contents;

}
