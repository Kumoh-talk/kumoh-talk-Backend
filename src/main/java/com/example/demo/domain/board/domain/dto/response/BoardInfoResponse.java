package com.example.demo.domain.board.domain.dto.response;

import com.example.demo.domain.board.domain.entity.Board;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;


import java.time.LocalDateTime;
import java.util.List;

@Getter
@Schema(name = "BoardInfoResponse", description = "게시물 상세 조회 응답")
public class BoardInfoResponse {
    @Schema(description = "게시물 ID", example = "1")
    private Long boardId;
    @Schema(description = "게시물 작성자", example = "user1")
    private String username;
    @Schema(description = "게시물 제목", example = "게시물 제목")
    private String title;
    @Schema(description = "게시물 내용", example = "게시물 내용")
    private String contents;
    @Schema(description = "게시물 태그 [SEMINAR/NOTICE]", example = "SEMINAR")
    private String tag;
    @Schema(description = "게시물 상태 [DRAFT/PUBLISHED]", example = "DRAFT")
    private String status;
    @Schema(description = "게시물 조회수", example = "100")
    private Long view;
    @Schema(description = "게시물 좋아요 수", example = "10")
    private Long like;
    @Schema(description = "게시물 카테고리", example = "['카테고리1','카테고리2']")
    private List<String> categoryNames;
    @Schema(description = "게시물 대표 이미지 URL", example = "https://s3.bucket/board/1.jpg")
    private String boardHeadImageUrl;
    @Schema(description = "게시물 수정일", example = "2021-08-01T00:00:00")
    private LocalDateTime updatedAt;
    @Schema(description = "게시물 생성일", example = "2021-08-01T00:00:00")
    private LocalDateTime createdAt;

    @Builder
    public BoardInfoResponse(Long boardId, String username, String title, String contents, String tag, String status, Long view, Long like, List<String> categoryNames,String boardHeadImageUrl, LocalDateTime updatedAt, LocalDateTime createdAt) {
        this.boardId = boardId;
        this.username = username;
        this.title = title;
        this.contents = contents;
        this.tag = tag;
        this.status = status;
        this.view = view;
        this.like = like;
        this.categoryNames = categoryNames;
        this.boardHeadImageUrl = boardHeadImageUrl;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public static BoardInfoResponse from(Board board, String username, Long view, Long like, List<String> categoryNames) {
        return BoardInfoResponse.builder()
                .boardId(board.getId())
                .username(username)
                .title(board.getTitle())
                .contents(board.getContent())
                .tag(board.getTag().name())
                .status(board.getStatus().name())
                .view(view)
                .like(like)
                .categoryNames(categoryNames)
                .boardHeadImageUrl(board.getHeadImageUrl())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .build();
    }

}
