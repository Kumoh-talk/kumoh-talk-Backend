package com.example.demo.domain.comment.entity;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CommentListInfo {
    private Long commentCount;
    private List<CommentInfo> commentInfo;

    @Builder
    public CommentListInfo(Long commentCount, List<CommentInfo> commentInfo) {
        this.commentCount = commentCount;
        this.commentInfo = commentInfo;
    }
}
