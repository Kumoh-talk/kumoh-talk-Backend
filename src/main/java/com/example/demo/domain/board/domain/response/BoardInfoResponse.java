package com.example.demo.domain.board.domain.response;

import com.example.demo.domain.board.domain.entity.Board;
import lombok.Builder;
import lombok.EqualsAndHashCode;
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
    private Long view;
    private Long like;
    private List<String> categoryNames;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    @Builder
    public BoardInfoResponse(Long boardId, String username, String title, String contents,  LocalDateTime updatedAt, LocalDateTime createdAt,Long view,Long like,List<String> categoryNames) {
        this.boardId = boardId;
        this.username = username;
        this.title = title;
        this.contents = contents;
        this.view = view;
        this.like = like;
        this.categoryNames = categoryNames;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }


    public static BoardInfoResponse from(Board board, String username,Long view,Long like,List<String> categoryNames) {
        return BoardInfoResponse.builder()
                .boardId(board.getId())
                .username(username)
                .title(board.getTitle())
                .contents(board.getContent())
                .view(view)
                .like(like)
                .categoryNames(categoryNames)
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .build();
    }

}
