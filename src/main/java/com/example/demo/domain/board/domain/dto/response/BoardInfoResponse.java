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
    private final Long boardId;
    private final String username;
    private final String title;
    private final String contents;
    private final String boardType;
    private final String status;
    private final Long view;
    private final Long like;
    private final List<String> categoryNames;
    private final String boardHeadImageUrl;
    private final LocalDateTime updatedAt;
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

    public static BoardInfoResponse from(Board board, String username, Long view, Long like, List<String> categoryNames) {
        return BoardInfoResponse.builder()
                .boardId(board.getId())
                .username(username)
                .title(board.getTitle())
                .contents(board.getContent())
                .tag(board.getBoardType().name())
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
