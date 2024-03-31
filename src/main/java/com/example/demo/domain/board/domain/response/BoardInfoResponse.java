package com.example.demo.domain.board.domain.response;

import com.example.demo.domain.board.domain.Board;
import com.example.demo.domain.file.domain.FileNameInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    private FileNameInfo attachFileNameInfo;
    private List<FileNameInfo> imageFileNameInfos;

    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    @Builder
    public BoardInfoResponse(Long boardId, String username, String title, String contents, FileNameInfo attachFileNameInfo, List<FileNameInfo> imageFileNameInfos, LocalDateTime updatedAt, LocalDateTime createdAt) {
        this.boardId = boardId;
        this.username = username;
        this.title = title;
        this.contents = contents;
        this.attachFileNameInfo = attachFileNameInfo;
        this.imageFileNameInfos = imageFileNameInfos;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }


    public static BoardInfoResponse from(Board board, String username, FileNameInfo attachFileNameInfo, List<FileNameInfo> imageFileNameInfos) {
        return new BoardInfoResponse(
                board.getId(),
                username,
                board.getTitle(),
                board.getContent(),
                attachFileNameInfo,
                imageFileNameInfos,
                board.getUpdatedAt(),
                board.getCreatedAt()
        );
    }

}
