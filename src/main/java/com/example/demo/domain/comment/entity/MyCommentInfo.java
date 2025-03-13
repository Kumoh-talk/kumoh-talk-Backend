package com.example.demo.domain.comment.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
@Builder
public class MyCommentInfo {

    private final Long commentId;
    private final String commentContent;
    private final LocalDateTime commentCreatedAt;
    private final LocalDateTime commentUpdatedAt;

    private final Long boardId;
    private final String boardTitle;
    private final LocalDateTime boardCreatedAt;
    private final LocalDateTime boardUpdatedAt;
}
