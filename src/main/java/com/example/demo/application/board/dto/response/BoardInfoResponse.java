package com.example.demo.application.board.dto.response;

import com.example.demo.infra.board.entity.Board;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;


import java.time.LocalDateTime;
import java.util.List;

@Getter
@Schema(name = "BoardInfoResponse", description = "게시물 상세 조회 응답")
public class BoardInfoResponse {
    @Schema(description = "게시물 ID", example = "1")
    private final Long boardId;
    @Schema(description = "게시물 작성자", example = "유저1")
    private final String username;
    @Schema(description = "게시물 제목", example = "백엔드 공부 로드맵!")
    private final String title;
    @Schema(description = "게시물 내용", example = "백엔드 공부를 위한 로드맵을 공유합니다.")
    private final String contents;
    @Schema(description = "게시물 유형", example = "NOTICE")
    private final String boardType;
    @Schema(description = "게시물 상태", example = "PUBLISHED")
    private final String status;
    @Schema(description = "게시물 조회수", example = "100")
    private final Long view;
    @Schema(description = "게시물 좋아요 수", example = "10")
    private final Long like;
    @Schema(
        description = "게시물 카테고리 이름 리스트",
        example = "[\"카테고리1\", \"카테고리2\"]"
    )
    private final List<String> categoryNames;
    @Schema(description = "게시물 대표 이미지 URL", example = "https://kumoh-talk-bucket.s3.ap-northeast-2.amazonaws.com/")
    private final String boardHeadImageUrl;
    @Schema(description = "게시물 수정 시간", example = "2024-09-11 10:30")
    private final LocalDateTime updatedAt;
    @Schema(description = "게시물 작성 시간", example = "2024-09-11 10:30")
    private final LocalDateTime createdAt;

    @Builder
    public BoardInfoResponse(Long boardId, String username, String title, String contents, String tag, String status, Long view, Long like, List<String> categoryNames,String boardHeadImageUrl, LocalDateTime updatedAt, LocalDateTime createdAt) {
        this.boardId = boardId;
        this.username = username;
        this.title = title;
        this.contents = contents;
        this.boardType = tag;
        this.status = status;
        this.view = view;
        this.like = like;
        this.categoryNames = categoryNames;
        this.boardHeadImageUrl = boardHeadImageUrl;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public static BoardInfoResponse from(Board board, String username,  Long like, List<String> categoryNames) {
        return BoardInfoResponse.builder()
                .boardId(board.getId())
                .username(username)
                .title(board.getTitle())
                .contents(board.getContent())
                .tag(board.getBoardType().name())
                .status(board.getStatus().name())
                .view(board.getViewCount())
                .like(like)
                .categoryNames(categoryNames)
                .boardHeadImageUrl(board.getHeadImageUrl())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .build();
    }

}
