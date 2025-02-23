package com.example.demo.fixture.board;

import com.example.demo.application.board.dto.vo.BoardType;
import com.example.demo.application.board.dto.vo.Status;
import com.example.demo.infra.board.entity.Board;
import com.example.demo.domain.user.domain.User;

public class BoardFixtures {

	/* 게시판 제목 */
	private static final String GENERAL_BOARD_TITLE = "title";

	/* 게시판 제목 */
	private static final String GENERAL_BOARD_CONTENT = "content";

	/* 게시판 헤드 이미지 URL */
	private static final String GENERAL_BOARD_HEAD_IMAGE_URL = "https://kumoh-talk-bucket.s3.ap-northeast-2.amazonaws.com/board/15/image/54599f59-1d5b-4167-b9f7-96f84d3c452d/example.jpg";

	/* 게시판 타입 (세미나/공지사항) */
	private static final BoardType SEMINAR_BOARD_TYPE = BoardType.SEMINAR;
	private static final BoardType NOTICE_BOARD_TYPE = BoardType.NOTICE;

	/* 게시판 상태 */
	private static final Status DRAFT_STATUS = Status.DRAFT;
	private static final Status PUBLISHED_STATUS = Status.PUBLISHED;

	public static Board PUBLISHED_SEMINAR_BOARD(User user) {
		return Board.builder()
				.title(GENERAL_BOARD_TITLE)
				.content(GENERAL_BOARD_CONTENT)
				.boardType(SEMINAR_BOARD_TYPE)
				.user(user)
				.status(PUBLISHED_STATUS)
				.headImageUrl(GENERAL_BOARD_HEAD_IMAGE_URL)
				.build();
	}

	public static Board DRAFT_SEMINAR_BOARD(User user) {
		return Board.builder()
				.title(GENERAL_BOARD_TITLE)
				.content(GENERAL_BOARD_CONTENT)
				.boardType(SEMINAR_BOARD_TYPE)
				.user(user)
				.status(DRAFT_STATUS)
				.headImageUrl(GENERAL_BOARD_HEAD_IMAGE_URL)
				.build();
	}
}
