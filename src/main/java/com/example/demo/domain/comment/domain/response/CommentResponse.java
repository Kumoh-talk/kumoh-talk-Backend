package com.example.demo.domain.comment.domain.response;

import com.example.demo.domain.comment.domain.entity.Comment;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CommentResponse {
    private int commentsCount;
    private List<CommentInfo> commentInfoList;

    public static CommentResponse from(List<CommentInfo> commentInfoList){
        int replyCommentsCount = 0;
        for (CommentInfo commentInfo : commentInfoList){
            replyCommentsCount += commentInfo.getReplyComments().size();
        }
        return new CommentResponse(replyCommentsCount+commentInfoList.size(), commentInfoList);
    }
}
