package com.example.demo.domain.board.domain.page;

import com.example.demo.domain.board.domain.entity.Board;
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
    private LocalDateTime createdAt; // 작성 시간

    @Builder // TODO : Track 정보가 인자로 있는데, 안써서 뺌..
    public PageTitleInfo(Long postId, String title, String userName,LocalDateTime createdAt) {
        this.postId = postId;
        this.title = title;
        this.userName = userName;
        this.createdAt = createdAt;
    }

    public static PageTitleInfo from(Board board, String userName){
        return PageTitleInfo.builder()
                .postId(board.getId())
                .title(board.getTitle())
                .userName(userName)
                .createdAt(board.getCreatedAt())
                .build();
    }
}
