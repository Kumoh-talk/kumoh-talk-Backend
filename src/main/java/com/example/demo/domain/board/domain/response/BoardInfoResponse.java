package com.example.demo.domain.board.domain.response;

import com.example.demo.domain.board.domain.entity.Board;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardInfoResponse {
    private Long boardId;
    private String username;
    private String title;
    private String contents;

    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    @Builder
    public BoardInfoResponse(Long boardId, String username, String title, String contents,  LocalDateTime updatedAt, LocalDateTime createdAt) {
        this.boardId = boardId;
        this.username = username;
        this.title = title;
        this.contents = contents;

        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }


    public static BoardInfoResponse from(Board board, String username) {
        return new BoardInfoResponse(
                board.getId(),
                username,
                board.getTitle(),
                board.getContent(),
                board.getUpdatedAt(),
                board.getCreatedAt()
        );
    }

}
