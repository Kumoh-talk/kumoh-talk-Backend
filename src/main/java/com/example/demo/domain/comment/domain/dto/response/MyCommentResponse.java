package com.example.demo.domain.comment.domain.dto.response;

import com.example.demo.domain.comment.domain.entity.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
@Schema(name = "MyCommentResponse", description = "사용자 작성 댓글 상세 정보 응답")
public class MyCommentResponse {
    @Schema(description = "사용자 작성 댓글 정보")
    private MyCommentInfoResponse comment;
    @Schema(description = "댓글을 단 게시물 정보")
    private CommentBoardInfoResponse board;

    public static MyCommentResponse fromSeminarComment(Comment commentEntity) {
        return MyCommentResponse.builder()
                .comment(MyCommentInfoResponse.builder()
                        .id(commentEntity.getId())
                        .content(commentEntity.getContent())
                        .createdAt(commentEntity.getCreatedAt())
                        .updatedAt(commentEntity.getUpdatedAt())
                        .build())
                .board(CommentBoardInfoResponse.builder()
                        .id(commentEntity.getBoard().getId())
                        .title(commentEntity.getBoard().getTitle())
                        .createdAt(commentEntity.getBoard().getCreatedAt())
                        .updatedAt(commentEntity.getBoard().getUpdatedAt())
                        .build())
                .build();
    }

    public static MyCommentResponse fromRecruitmentComment(com.example.demo.domain.comment.domain.entity.Comment commentEntity) {
        return MyCommentResponse.builder()
                .comment(MyCommentInfoResponse.builder()
                        .id(commentEntity.getId())
                        .content(commentEntity.getContent())
                        .createdAt(commentEntity.getCreatedAt())
                        .updatedAt(commentEntity.getUpdatedAt())
                        .build())
                .board(CommentBoardInfoResponse.builder()
                        .id(commentEntity.getRecruitmentBoard().getId())
                        .createdAt(commentEntity.getRecruitmentBoard().getCreatedAt())
                        .updatedAt(commentEntity.getRecruitmentBoard().getUpdatedAt())
                        .build())
                .build();
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @Schema(name = "MyCommentInfoResponse", description = "사용자 작성 댓글 정보")
    public static class MyCommentInfoResponse {
        @Schema(description = "사용자 작성 댓글 아이디", example = "3")
        private Long id;
        @Schema(description = "사용자 작성 댓글 내용", example = "this is content")
        private String content;
        @Schema(description = "사용자 작성 댓글 작성 날짜 및 시간", example = "\"2024-11-18T17:09:25\"")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime createdAt;
        @Schema(description = "사용자 작성 댓글 수정 날짜 및 시간", example = "null")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime updatedAt;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @Schema(name = "CommentBoardInfoResponse", description = "댓글을 단 게시물 정보")
    public static class CommentBoardInfoResponse {
        @Schema(description = "댓글을 단 게시물 아이디", example = "3")
        private Long id;
        @Schema(description = "댓글을 단 게시물 제목", example = "board title")
        private String title;
        @Schema(description = "댓글을 단 게시물 작성 날짜 및 시간", example = "\"2024-11-18T17:09:25\"")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime createdAt;
        @Schema(description = "댓글을 단 게시물 수정 날짜 및 시간", example = "\"2024-11-18T17:09:25\"")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime updatedAt;
    }
}