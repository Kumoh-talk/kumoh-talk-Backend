package com.example.demo.application.board.dto.response;

import java.time.LocalDateTime;

import com.example.demo.application.board.dto.vo.BoardType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(name = "BoardTitleInfoResponse", description = "게시물 제목 정보 응답")
public class BoardTitleInfoResponse {
	@Schema(description = "게시글 아이디", example = "1")
	private Long boardId;
	@Schema(description = "게시글 제목", example = "게시글 제목")
	private String title;
	@Schema(description = "게시글 작성자", example = "user1")
	private String userName;
	@Schema(description = "게시글 태그 [SEMINAR/NOTICE]", example = "SEMINAR")
	private String boardTag;
	@Schema(description = "게시글 조회수", example = "100")
	private Long view;
	@Schema(description = "게시글 좋아요 수", example = "10")
	private Long like;
	@Schema(description = "게시물 대표 이미지 URL", example = "https://kumoh-talk-bucket.s3.ap-northeast-2.amazonaws.com/board/15/image/54599f59-1d5b-4167-b9f7-96f84d3c452d/example.jpg")
	private String headImageUrl;
	@Schema(description = "게시글 생성일", example = "2021-08-01T00:00:00")
	private String createdAt;

	public BoardTitleInfoResponse(Long boardId, String title, String userName, BoardType boardType,Long view, Long like,String headImageUrl ,LocalDateTime createdAt) {
		this.boardId = boardId;
		this.title = title;
		this.userName = userName;
		this.boardTag = boardType.toString();
		this.view = view;
		this.like = like;
		this.headImageUrl = headImageUrl;
		this.createdAt = createdAt.toString();
	}
}
