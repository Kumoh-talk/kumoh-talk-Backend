package com.example.demo.domain.board.service.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LikeInfo {
    private Long likeId;
    private Long userId;
    private String userNickname;
    private Long boardId;

    @Builder
    public LikeInfo(Long likeId, Long userId, String userNickname, Long boardId) {
        this.likeId = likeId;
        this.userId = userId;
        this.userNickname = userNickname;
        this.boardId = boardId;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }
}
