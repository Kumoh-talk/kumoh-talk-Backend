package com.example.demo.domain.comment.domain.dto.response;

import com.example.demo.domain.comment.domain.entity.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentInfoResponse {
    private Long commentId;
    private Long groupId;
    private String userNickname;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime deletedAt;
    private List<CommentInfoResponse> replyComments;

    protected CommentInfoResponse(Long commentId, String userNickname, String content,
                                  LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.commentId = commentId;
        this.userNickname = userNickname;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static CommentInfoResponse from(Comment commentEntity) {
        CommentInfoResponse commentInfoResponse = new CommentInfoResponse(commentEntity.getId(),
                commentEntity.getUser().getNickname(),
                commentEntity.getContent(),
                commentEntity.getCreatedAt(),
                commentEntity.getUpdatedAt(),
                commentEntity.getDeletedAt()
        );
        if (commentEntity.getParentComment() != null) {
            commentInfoResponse.setGroupId(commentEntity.getParentComment().getId());
        }
        commentInfoResponse.setReplyComments(new ArrayList<>(commentEntity.getReplyComments().stream()
                .map(CommentInfoResponse::from).collect(Collectors.toList())));
        return commentInfoResponse;
    }
}
