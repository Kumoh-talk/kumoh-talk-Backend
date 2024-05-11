package com.example.demo.domain.comment.domain.response;

import com.example.demo.domain.comment.domain.entity.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CommentInfo {
    private Long commentId;
    private Long groupId;
    private int depth;
    private String username;
    private String contents;
    private int likedCount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime deletedAt;
    public static CommentInfo from(Comment comment) {
        return new CommentInfo(comment.getId(),
                comment.getParentComment().getId(),
                comment.getDepth(),
                comment.getUser().getName(),
                comment.getContent(),
                comment.getLikedUsers().size(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                comment.getDeletedAt());
    }
}
