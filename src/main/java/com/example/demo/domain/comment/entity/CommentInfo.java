package com.example.demo.domain.comment.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CommentInfo {
    private Long commentId;
    private Long groupId;
    private CommentUserInfo commentUserInfo;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private List<CommentInfo> replyComments;

    private Long boardId;

    @Builder
    public CommentInfo(Long commentId, Long groupId, CommentUserInfo commentUserInfo, String content, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt, List<CommentInfo> replyComments, Long boardId) {
        this.commentId = commentId;
        this.groupId = groupId;
        this.commentUserInfo = commentUserInfo;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.replyComments = replyComments;
        this.boardId = boardId;
    }
}
