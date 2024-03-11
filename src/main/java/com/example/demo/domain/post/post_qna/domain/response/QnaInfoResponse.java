package com.example.demo.domain.post.post_qna.domain.response;

import com.example.demo.domain.post.domain.Post;
import com.example.demo.domain.post.domain.response.PostInfoResponse;
import com.example.demo.domain.post.post_qna.domain.Post_Qna;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
public class QnaInfoResponse {
    private Long qnaId;
    private String title;
    private String contents;

    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime updatedAt;


    public static QnaInfoResponse from(Post_Qna qna) {
        return new QnaInfoResponse(
                qna.getId(),
                qna.getTitle(),
                qna.getContents(),
                qna.getUpdatedAt()
        );
    }
}
