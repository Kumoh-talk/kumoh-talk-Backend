package com.example.demo.domain.board.controller;

import static com.example.demo.infra.fixture.board.BoardFixtures.*;
import static com.example.demo.fixture.user.UserFixtures.*;
import static org.assertj.core.api.SoftAssertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.example.demo.base.IntegrationTest;
import com.example.demo.domain.board.service.entity.BoardContent;
import com.example.demo.domain.board.service.entity.BoardInfo;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.UserTarget;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.domain.board.service.view.BoardViewEvent;
import com.example.demo.infra.board.entity.Board;

public class BoardServiceIntegrationTest extends IntegrationTest {


	@Nested
	@DisplayName("<세미나/공지사항 게시물 상세 조회>")
	class Describe_getSeminarNoticeBoardDetail {
		private User savedUser;

		private static final String BOARD_VIEW_KEY = "board:view:";

		@BeforeEach
		void setUp() {
			savedUser = testFixtureBuilder.buildUser(ADMIN_USER());
		}

		@AfterEach
		void tearDown() {
			redisTemplate.opsForList().getOperations().delete("board:view:");
		}

		@Test
		void 성공_조휘수캐싱_ApplicationEvent_데이터를_조회할수있다() {
			// given
			Board publishedBOARD = testFixtureBuilder.buildBoard(PUBLISHED_SEMINAR_BOARD(savedUser));

			// when
			BoardInfo boardInfo = boardService.searchSingleBoard(null,publishedBOARD.getId());
			BoardContent boardContent = boardInfo.getBoardContent();

			// then
			assertSoftly(softly -> {
				softly.assertThat(boardInfo.getBoardId()).isEqualTo(publishedBOARD.getId());
				softly.assertThat(boardContent.getTitle()).isEqualTo(publishedBOARD.getTitle());
				softly.assertThat(boardContent.getContents()).isEqualTo(publishedBOARD.getContent());
				softly.assertThat(boardContent.getBoardType()).isEqualTo(publishedBOARD.getBoardType());
				softly.assertThat(boardContent.getBoardStatus()).isEqualTo(publishedBOARD.getStatus());
				softly.assertThat(boardInfo.getViewCount()).isEqualTo(publishedBOARD.getViewCount());
				softly.assertThat(boardInfo.getLikeCount()).isEqualTo(publishedBOARD.getLikes().size());
				softly.assertThat(boardInfo.getCreatedAt()).isEqualTo(publishedBOARD.getCreatedAt());
				softly.assertThat(boardInfo.getUpdatedAt()).isEqualTo(publishedBOARD.getUpdatedAt());
			});

			assertSoftly(softly -> {
				softly.assertThat(redisTemplate.opsForValue().get(BOARD_VIEW_KEY + publishedBOARD.getId()))
					.isEqualTo("1");
				softly.assertThat(events.stream(BoardViewEvent.class).count()).isEqualTo(1);
			});

		}

		@Test
		void 성공_임시저장_게시물은_게시물_소유자만_조회할_수_있다() {
			// given
			Board draftBoard = testFixtureBuilder.buildBoard(DRAFT_SEMINAR_BOARD(savedUser));

			// when
			BoardInfo boardInfo = boardService.searchSingleBoard(savedUser.getId(),draftBoard.getId());
			BoardContent boardContent = boardInfo.getBoardContent();
			UserTarget userTarget = boardInfo.getUserTarget();
			// then
			assertSoftly(softly -> {
				softly.assertThat(boardInfo.getBoardId()).isEqualTo(draftBoard.getId());
				softly.assertThat(boardContent.getTitle()).isEqualTo(draftBoard.getTitle());
				softly.assertThat(boardContent.getContents()).isEqualTo(draftBoard.getContent());
				softly.assertThat(boardContent.getBoardType()).isEqualTo(draftBoard.getBoardType());
				softly.assertThat(boardContent.getBoardStatus()).isEqualTo(draftBoard.getStatus());
				softly.assertThat(boardInfo.getViewCount()).isEqualTo(draftBoard.getViewCount());
				softly.assertThat(boardInfo.getLikeCount()).isEqualTo(draftBoard.getLikes().size());
				softly.assertThat(boardInfo.getCreatedAt()).isEqualTo(draftBoard.getCreatedAt());
				softly.assertThat(boardInfo.getUpdatedAt()).isEqualTo(draftBoard.getUpdatedAt());
				softly.assertThat(userTarget.getUserId()).isEqualTo(savedUser.getId());
				softly.assertThat(userTarget.getNickName()).isEqualTo(savedUser.getNickname());
				softly.assertThat(userTarget.getUserRole()).isEqualTo(savedUser.getRole());
			});

			assertSoftly(softly -> {
				softly.assertThat(redisTemplate.opsForValue().get( BOARD_VIEW_KEY+ draftBoard.getId()))
					.isEqualTo("1");
				softly.assertThat(events.stream(BoardViewEvent.class).count()).isEqualTo(1);
			});
		}

		@Test
		void 실패_임시저장_게시물은_게시물_소유자가_아니면_실패한다() {
			// given
			Board draftBoard = testFixtureBuilder.buildBoard(DRAFT_SEMINAR_BOARD(savedUser));



			// when -> then
			assertSoftly(softly -> {
				softly.assertThatThrownBy(() -> boardService.searchSingleBoard(999L,draftBoard.getId()))
					.isInstanceOf(ServiceException.class)
					.hasFieldOrPropertyWithValue("errorCode", ErrorCode.DRAFT_NOT_ACCESS_USER);
				softly.assertThat(redisTemplate.opsForValue().get(BOARD_VIEW_KEY + draftBoard.getId()))
					.isNull();
				softly.assertThat(events.stream(BoardViewEvent.class).count()).isEqualTo(0);
			});
		}

		@Test
		void 실패_유효하지_않는_게시물_조회() {
			// given
			Board draftBoard = testFixtureBuilder.buildBoard(DRAFT_SEMINAR_BOARD(savedUser));

			// when -> then
			assertSoftly(softly -> {
				softly.assertThatThrownBy(() -> boardService.searchSingleBoard(savedUser.getId(), draftBoard.getId() + 1))
					.isInstanceOf(ServiceException.class)
					.hasFieldOrPropertyWithValue("errorCode", ErrorCode.BOARD_NOT_FOUND);
				softly.assertThat(redisTemplate.opsForValue().get(BOARD_VIEW_KEY + draftBoard.getId()))
					.isNull();
				softly.assertThat(events.stream(BoardViewEvent.class).count()).isEqualTo(0);
			});

		}
	}

}
