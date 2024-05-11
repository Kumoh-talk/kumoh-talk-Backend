package com.example.demo.domain.comment.domain.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CommentResponse {
    private int commentsCount;
    private List<CommentInfo> commentInfoList;

    public static CommentResponse from(List<CommentInfo> commentInfoList){
        return new CommentResponse(commentInfoList.size(), commentInfoList);
    }
}
