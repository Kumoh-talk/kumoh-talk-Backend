package com.example.demo.domain.post.post_qna.domain.request;


import com.example.demo.domain.post.comment.domain.Comment;
import com.example.demo.domain.post.domain.File;
import com.example.demo.domain.post.domain.Post;
import com.example.demo.domain.post.post_qna.domain.Post_Qna;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.vo.Track;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QnaSaveRequest {

    @NotBlank(message = " 게시글의 고유 id가 누락 되었습니다.")
    private Long postId;

    @NotBlank(message = "제목은 필수 항목입니다.")
    @Max(value = 45,message = "최대 제한 45글자 입니다.")
    private String title;

    @NotBlank(message = "댓글 내용은 필수 항목입니다.")
    @Max(value = 45,message = "최대 제한 500글자 입니다.")
    private String contents;

    public static Post_Qna toEntity(QnaSaveRequest qnaSaveRequest, Post post) {
        return new Post_Qna(qnaSaveRequest.getTitle(), qnaSaveRequest.getContents(), post);
    }



}
