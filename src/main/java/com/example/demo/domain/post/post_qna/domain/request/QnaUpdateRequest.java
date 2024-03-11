package com.example.demo.domain.post.post_qna.domain.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class QnaUpdateRequest {

    @NotBlank(message = " 게시글의 고유 id가 누락 되었습니다.")
    private Long qnaId;

    @NotBlank(message = "제목은 필수 항목입니다.")
    @Max(value = 45,message = "최대 제한 45글자 입니다.")
    private String title;

    @NotBlank(message = "댓글 내용은 필수 항목입니다.")
    @Max(value = 45,message = "최대 제한 500글자 입니다.")
    private String contents;
}
