package com.example.demo.domain.post_qna.domain.request;


import com.example.demo.domain.post.domain.Post;
import com.example.demo.domain.post_qna.domain.Post_Qna;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QnaRequest {


    @NotBlank(message = "제목은 필수 항목입니다.")
    @Max(value = 45,message = "최대 제한 45글자 입니다.")
    private String title;

    @NotBlank(message = "댓글 내용은 필수 항목입니다.")
    @Max(value = 500,message = "최대 제한 500글자 입니다.")
    private String contents;

    public static Post_Qna toEntity(QnaRequest qnaRequest, Post post) {
        return new Post_Qna(qnaRequest.getTitle(), qnaRequest.getContents(), post);
    }



}
