package com.example.demo.fixture.board;

import java.util.List;

import com.example.demo.application.board.dto.vo.BoardType;
import com.example.demo.application.board.dto.vo.Status;
import com.example.demo.domain.board.service.entity.BoardCategoryNames;
import com.example.demo.domain.board.service.entity.BoardContent;

public class BoardDomainFixtures {
	private static final String BOARD_CONTENT_TITLE = "title";
	private static final String BOARD_CONTENT_CONTENTS = "contents";
	private static final String BOARD_CONTENT_HEAD_IMAGE_URL = "headImageUrl";
	private static final BoardType BOARD_CONTENT_TYPE_SEMINAR = BoardType.SEMINAR;
	private static final BoardType BOARD_CONTENT_TYPE_NOTICE = BoardType.NOTICE;
	private static final Status BOARD_CONTENT_STATUS_DRAFT = Status.DRAFT;
	private static final Status BOARD_CONTENT_STATUS_PUBLISHED = Status.PUBLISHED;

	private static final List<String> BOARD_CATEGORY_NAME_LIST = List.of("category1", "category2");

	public static BoardContent BOARD_SEMINAR_DRAFT_CONTENT = new BoardContent(BOARD_CONTENT_TITLE, BOARD_CONTENT_CONTENTS, BOARD_CONTENT_TYPE_SEMINAR, BOARD_CONTENT_HEAD_IMAGE_URL, BOARD_CONTENT_STATUS_DRAFT);
	public static BoardContent BOARD_NOTICE_DRAFT_CONTENT = new BoardContent(BOARD_CONTENT_TITLE, BOARD_CONTENT_CONTENTS, BOARD_CONTENT_TYPE_NOTICE, BOARD_CONTENT_HEAD_IMAGE_URL, BOARD_CONTENT_STATUS_DRAFT);


	public static BoardCategoryNames BOARD_CATEGORY_NAMES = new BoardCategoryNames(BOARD_CATEGORY_NAME_LIST);

	public static BoardContent UPDATE_BOARD_CONTENT = new BoardContent(BOARD_CONTENT_TITLE, BOARD_CONTENT_CONTENTS, null, BOARD_CONTENT_HEAD_IMAGE_URL, null);

}
