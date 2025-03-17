package com.example.demo.application.report.dto.response;

import com.example.demo.domain.report.domain.ReportedCommentInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(name = "ReportedCommentInfoResponse", description = "신고된 댓글 상세 정보 응답")
public record ReportedCommentInfoResponse (
    @Schema(description = "댓글 id", example = "3")
    Long commentId,

    @Schema(description = "댓글 작성자 id", example = "3")
    Long userId,

    @Schema(description = "댓글 작성자 프로필 이미지 URL", example = "https://kumoh-talk-bucket.s3.ap-northeast-2.amazonaws.com/profile/default_profile.png")
    String userProfileImageUrl,

    @Schema(description = "댓글 작성자 닉네임", example = "kumoh")
    String userNickname,

    @Schema(description = "댓글 내용", example = "this is content")
    String content,

    @Schema(description = "댓글 작성 날짜 및 시간", example = "\"2024-11-17T09:08:05\"")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime createdAt,

    @Schema(description = "댓글 수정 날짜 및 시간", example = "\"2024-11-18T17:09:25\"")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime updatedAt,

    @Schema(description = "댓글 삭제 날짜 및 시간", example = "null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime deletedAt
) {
    public static ReportedCommentInfoResponse from(ReportedCommentInfo comment) {
        return ReportedCommentInfoResponse.builder()
                .commentId(comment.getCommentId())
                .userId(comment.getCommentUserInfo().getUserId())
                .userProfileImageUrl(comment.getCommentUserInfo().getProfileImageUrl())
                .userNickname(comment.getCommentUserInfo().getNickName())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .deletedAt(comment.getDeletedAt())
                .build();
    }
}
