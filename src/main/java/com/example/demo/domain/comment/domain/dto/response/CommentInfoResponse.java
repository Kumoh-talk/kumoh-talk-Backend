package com.example.demo.domain.comment.domain.dto.response;

import com.example.demo.domain.comment.domain.entity.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "CommentInfoResponse", description = "댓글 상세 정보 응답")
public class CommentInfoResponse {
    @Schema(description = "댓글 id", example = "3")
    private Long commentId;
    @Schema(description = "부모 댓글 id", example = "null")
    private Long groupId;
    @Schema(description = "댓글 작성자 닉네임", example = "kumoh")
    private String userNickname;
    @Schema(description = "댓글 내용", example = "this is content")
    private String content;
    @Schema(description = "댓글 작성 날짜 및 시간", example = "\"2024-11-17T09:08:05\"")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    @Schema(description = "댓글 수정 날짜 및 시간", example = "\"2024-11-18T17:09:25\"")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
    @Schema(description = "댓글 삭제 날짜 및 시간", example = "null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime deletedAt;
    @Schema(description = "대댓글 정보 리스트(위의 댓글 응답 양식으로 대댓글 정보가 나열됨)", example = "[]")
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

    public static CommentInfoResponse fromComment(Comment comment) {
        CommentInfoResponse commentInfoResponse = new CommentInfoResponse(
                comment.getId(),
                comment.getUser().getNickname(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                comment.getDeletedAt()
        );
        if (comment.getParentComment() != null) {
            commentInfoResponse.setGroupId(comment.getParentComment().getId());
        }
        commentInfoResponse.setReplyComments(new ArrayList<>(comment.getReplyComments().stream()
                .map(CommentInfoResponse::fromComment).collect(Collectors.toList())));
        return commentInfoResponse;
    }
}
