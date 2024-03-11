package com.example.demo.domain.post.comment.domain.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentUpdateRequest {
    @NotBlank(message = "comment 고유 id이 입력 안되었습니다.")
    private Long commentId;

    @NotBlank(message = "내용은 필수 항목입니다.")
    @Max(value = 500,message = "최대 제한 500글자 입니다.")
    private String contents;


}
