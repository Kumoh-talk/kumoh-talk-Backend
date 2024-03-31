package com.example.demo.domain.board.domain.page;

import com.example.demo.domain.board.domain.Board;
import com.example.demo.domain.user.domain.vo.Track;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class PageTitleInfo {
    private Long postId;
    private String title;
    private String userName;
    private Track track;
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime createdAt; // 작성 시간

    @Builder
    public PageTitleInfo(Long postId, String title, String userName, Track track, LocalDateTime createdAt) {
        this.postId = postId;
        this.title = title;
        this.userName = userName;
        this.track = track;
        this.createdAt = createdAt;
    }

    public static PageTitleInfo from(Board board, String userName){
        return PageTitleInfo.builder()
                .postId(board.getId())
                .title(board.getTitle())
                .userName(userName)
                .track(board.getTrack())
                .createdAt(board.getCreatedAt())
                .build();
    }
}
