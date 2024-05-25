package com.example.demo.domain.comment.domain.response;

import com.example.demo.domain.comment.domain.entity.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentInfo {
    private Long commentId;
    private Long groupId;
    private String userNickname;
    private String contents;
    private int likedCount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime deletedAt;
    private List<CommentInfo> replyComments;

    protected CommentInfo(Long commentId, String userNickname, String contents,
                       int likedCount, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.commentId = commentId;
        this.userNickname = userNickname;
        this.contents = contents;
        this.likedCount = likedCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static CommentInfo from(Comment comment) {
        CommentInfo commentInfo = new CommentInfo(comment.getId(),
                comment.getUser().getNickname(),
                comment.getContent(),
                comment.getLikedUsers().size(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                comment.getDeletedAt()
                );
        if (comment.getParentComment() != null)
            commentInfo.setGroupId(comment.getParentComment().getId());
        commentInfo.setReplyComments(new ArrayList<>(comment.getReplyComments().stream()
                .map(CommentInfo::from).collect(Collectors.toList())));
        return commentInfo;
    }
}
