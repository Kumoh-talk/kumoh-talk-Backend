package com.example.demo.domain.comment.domain.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {
    @NotBlank(message = "내용은 필수 항목입니다.")
    @Max(value = 500,message = "최대 제한 500글자 입니다.")
    private String contents;

    private Long groupId;

    private int depth;
}
