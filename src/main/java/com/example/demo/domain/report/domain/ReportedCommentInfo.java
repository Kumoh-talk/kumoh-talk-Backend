package com.example.demo.domain.report.domain;

import com.example.demo.domain.comment.entity.CommentUserInfo;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ReportedCommentInfo {
    private Long commentId;
    private CommentUserInfo commentUserInfo;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    private Long boardId;

    @Builder
    public ReportedCommentInfo(Long commentId, CommentUserInfo commentUserInfo, String content, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt, List<com.example.demo.domain.comment.entity.CommentInfo> replyComments, Long boardId) {
        this.commentId = commentId;
        this.commentUserInfo = commentUserInfo;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.boardId = boardId;
    }
}
