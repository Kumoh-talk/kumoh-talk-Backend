package com.example.demo.domain.comment.domain.response;

import com.example.demo.domain.comment.domain.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class CommentResponse {
    private int commentsCount;
    private List<CommentInfoResponse> commentInfoResponseList;

    public static CommentResponse from(List<Comment> commentEntityList) {
        List<CommentInfoResponse> commentInfoResponseList = commentEntityList.stream()
                .map(CommentInfoResponse::from)
                .collect(Collectors.toList());

        int replyCommentsCount = 0;
        for (CommentInfoResponse commentInfoResponse : commentInfoResponseList) {
            replyCommentsCount += commentInfoResponse.getReplyComments().size();
        }
        return new CommentResponse(replyCommentsCount + commentInfoResponseList.size(), commentInfoResponseList);
    }
}
