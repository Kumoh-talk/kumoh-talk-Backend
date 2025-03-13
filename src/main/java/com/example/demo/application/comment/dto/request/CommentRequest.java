package com.example.demo.application.comment.dto.request;

import com.example.demo.domain.comment.entity.CommentInfo;
import com.example.demo.domain.comment.entity.CommentUserInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "댓글 요청")
public class CommentRequest {
    @Schema(description = "댓글 내용 정보", example = "comment content")
    @NotBlank(message = "내용은 필수 항목입니다.")
    @Size(min = 1, max = 500, message = "댓글 내용 최대 길이는 500글자 입니다.")
    private String content;

    @Schema(description = "부모 댓글 정보", example = "null")
    private Long groupId;

    public CommentInfo toDomainForPost(Long userId, Long boardId) {
        return CommentInfo.builder()
                .content(content)
                .groupId(groupId)
                .commentUserInfo(CommentUserInfo.builder()
                        .userId(userId)
                        .build())
                .boardId(boardId)
                .build();
    }

    public CommentInfo toDomainForPatch(Long userId, Long commentId) {
        return CommentInfo.builder()
                .commentId(commentId)
                .content(content)
                .groupId(groupId)
                .commentUserInfo(CommentUserInfo.builder()
                        .userId(userId)
                        .build())
                .build();
    }
}
