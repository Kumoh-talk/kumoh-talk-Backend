package com.example.demo.domain.board.domain.response;

import com.example.demo.domain.board.domain.entity.Board;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardInfoResponse {
    private Long boardId;
    private String username; // TODO : username 이 필드명 맞는지 다시 봐야함  게시물은 실명 댓글은 별명이어서
    private String title;
    private String contents;
    private Long view;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    @Builder
    public BoardInfoResponse(Long boardId, String username, String title, String contents,  LocalDateTime updatedAt, LocalDateTime createdAt,Long view) {
        this.boardId = boardId;
        this.username = username;
        this.title = title;
        this.contents = contents;
        this.view = view;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }


    public static BoardInfoResponse from(Board board, String username,Long view) {
        return BoardInfoResponse.builder()
                .boardId(board.getId())
                .username(username)
                .title(board.getTitle())
                .contents(board.getContent())
                .view(view)
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .build();
    }

}
