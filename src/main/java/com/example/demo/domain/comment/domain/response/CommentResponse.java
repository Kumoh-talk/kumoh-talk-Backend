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
    private List<CommentInfo> commentInfoList;

    public static CommentResponse from(List<Comment> commentEntityList) {
        List<CommentInfo> commentInfoList = commentEntityList.stream()
                .map(CommentInfo::from)
                .collect(Collectors.toList());

        int replyCommentsCount = 0;
        for (CommentInfo commentInfo : commentInfoList) {
            replyCommentsCount += commentInfo.getReplyComments().size();
        }
        return new CommentResponse(replyCommentsCount + commentInfoList.size(), commentInfoList);
    }
}
