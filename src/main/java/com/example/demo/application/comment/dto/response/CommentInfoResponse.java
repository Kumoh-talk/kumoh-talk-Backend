package com.example.demo.application.comment.dto.response;

import com.example.demo.domain.comment.entity.CommentInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

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
    @Schema(description = "댓글 작성자 id", example = "3")
    private Long userId;
    @Schema(description = "댓글 작성자 프로필 이미지 URL", example = "https://kumoh-talk-bucket.s3.ap-northeast-2.amazonaws.com/profile/default_profile.png")
    private String userProfileImageUrl;
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

    @Builder
    protected CommentInfoResponse(Long commentId, Long groupId, Long userId, String userProfileImageUrl, String userNickname, String content,
                                  LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.commentId = commentId;
        this.groupId = groupId;
        this.userId = userId;
        this.userProfileImageUrl = userProfileImageUrl;
        this.userNickname = userNickname;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static CommentInfoResponse from(CommentInfo comment) {
        CommentInfoResponse commentInfoResponse = CommentInfoResponse.builder()
                .commentId(comment.getCommentId())
                .groupId(comment.getGroupId())
                .userId(comment.getCommentUserInfo().getUserId())
                .userProfileImageUrl(comment.getCommentUserInfo().getProfileImageUrl())
                .userNickname(comment.getCommentUserInfo().getNickName())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .deletedAt(comment.getDeletedAt())
                .build();

        commentInfoResponse.setReplyComments(new ArrayList<>(comment.getReplyComments().stream()
                .map(CommentInfoResponse::from).collect(Collectors.toList())));
        return commentInfoResponse;
    }
}
