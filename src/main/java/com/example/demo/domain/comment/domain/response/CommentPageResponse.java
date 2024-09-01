package com.example.demo.domain.comment.domain.response;

import com.example.demo.domain.comment.domain.entity.Comment;
import com.example.demo.domain.study_project_board.domain.dto.vo.BoardType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
public class CommentPageResponse {
    private final int pageSize;
    private final int pageNum;
    private final int totalPage;
    private final String pageSort;
    private final List<MyCommentResponse> myCommentResponseList;

    public static CommentPageResponse from(Page<Comment> commentEntityPage, BoardType boardType) {
        List<MyCommentResponse> myCommentResponseList;
        switch (boardType) {
            case SEMINAR -> myCommentResponseList = commentEntityPage.stream()
                    .map(MyCommentResponse::fromSeminarComment)
                    .collect(Collectors.toList());
            case STUDY, PROJECT -> myCommentResponseList = commentEntityPage.stream()
                    .map(MyCommentResponse::fromStudyProjectComment)
                    .collect(Collectors.toList());
            default -> throw new IllegalArgumentException("게시판 종류에 해당하는 값이 아닙니다.");
        }

        return CommentPageResponse.builder()
                .pageSize(commentEntityPage.getSize())
                .pageNum(commentEntityPage.getNumber() + 1)
                .totalPage(commentEntityPage.getTotalPages())
                .pageSort(commentEntityPage.getSort().toString())
                .myCommentResponseList(myCommentResponseList)
                .build();
    }
}
