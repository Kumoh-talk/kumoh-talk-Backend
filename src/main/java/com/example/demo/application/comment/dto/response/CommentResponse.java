package com.example.demo.application.comment.dto.response;

import com.example.demo.domain.comment.entity.CommentListInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@Schema(name = "CommentResponse", description = "댓글 정보 응답")
public class CommentResponse {
    @Schema(description = "댓글 전체 개수", example = "10")
    private Long commentsCount;
    @Schema(description = "댓글 정보 리스트")
    private List<CommentInfoResponse> commentInfoResponseList;

    public static CommentResponse from(CommentListInfo commentListInfo) {
        List<CommentInfoResponse> commentInfoResponseList = commentListInfo.getCommentInfo().stream()
                .map(CommentInfoResponse::from)
                .collect(Collectors.toList());

        return new CommentResponse(commentListInfo.getCommentCount(), commentInfoResponseList);
    }
}
