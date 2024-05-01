package com.example.demo.domain.comment.domain.response;

import com.example.demo.domain.comment.domain.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommentInfoResponse {
    private Long commentId;
    private String username;
    private String contents;

    public static CommentInfoResponse of(Comment comment, String username) {
        return new CommentInfoResponse(comment.getId(), username, comment.getContent());
    }
}
