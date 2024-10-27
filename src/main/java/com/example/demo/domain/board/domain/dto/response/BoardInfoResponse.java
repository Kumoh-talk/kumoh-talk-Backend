package com.example.demo.domain.board.domain.dto.response;

import com.example.demo.domain.board.domain.entity.Board;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class BoardInfoResponse {
    private Long boardId;
    private String username;
    private String title;
    private String contents;
    private String tag;
    private String status;
    private Long view;
    private Long like;
    private List<String> categoryNames;
    private String boardHeadImageUrl;
    private LocalDateTime updatedAt;
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
