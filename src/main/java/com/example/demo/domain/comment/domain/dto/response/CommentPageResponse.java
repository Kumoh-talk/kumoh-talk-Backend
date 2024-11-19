package com.example.demo.domain.comment.domain.dto.response;

import com.example.demo.domain.comment.domain.entity.Comment;
import com.example.demo.domain.recruitment_board.domain.vo.EntireBoardType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
@Schema(name = "CommentPageResponse", description = "사용자 작성 댓글 페이지 정보 응답")
public class CommentPageResponse {
    @Schema(description = "한 페이지에 댓글 갯수", example = "10")
    private final int pageSize;
    @Schema(description = "현재 페이지 number", example = "1")
    private final int pageNum;
    @Schema(description = "총 페이지 갯수", example = "10")
    private final int totalPage;
    @Schema(description = "정렬 기준 및 방향", example = "createdAt: DESC")
    private final String pageSort;
    @Schema(description = "사용자가 작성한 댓글 정보 리스트")
    private final List<MyCommentResponse> myCommentResponseList;

    public static CommentPageResponse from(Page<Comment> commentEntityPage, EntireBoardType entireBoardType) {
        List<MyCommentResponse> myCommentResponseList;
        switch (entireBoardType) {
            case SEMINAR_NOTICE, SEMINAR_SUMMARY -> myCommentResponseList = commentEntityPage.stream()
                    .map(MyCommentResponse::fromSeminarComment)
                    .collect(Collectors.toList());
            case STUDY, PROJECT, MENTORING -> myCommentResponseList = commentEntityPage.stream()
                    .map(MyCommentResponse::fromRecruitmentComment)
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
