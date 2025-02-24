package com.example.demo.domain.board.controller;




import static com.example.demo.fixture.board.BoardFixtures.*;
import static com.example.demo.fixture.user.UserFixtures.*;
import static org.assertj.core.api.SoftAssertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.demo.base.IntegrationTest;
import com.example.demo.application.board.dto.response.BoardInfoResponse;
import com.example.demo.infra.board.entity.Board;
import com.example.demo.domain.user.domain.User;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.global.event.view.BoardViewEvent;
import com.example.demo.global.jwt.JwtAuthentication;
import com.example.demo.global.jwt.JwtUserClaim;

public class BoardControllerIntegrationTest extends IntegrationTest {


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
			BoardInfoResponse boardInfoResponse = boardService.searchSingleBoard(publishedBOARD.getId());

			// then
			assertSoftly(softly -> {
				softly.assertThat(boardInfoResponse.getBoardId()).isEqualTo(publishedBOARD.getId());
				softly.assertThat(boardInfoResponse.getTitle()).isEqualTo(publishedBOARD.getTitle());
				softly.assertThat(boardInfoResponse.getContents()).isEqualTo(publishedBOARD.getContent());
				softly.assertThat(boardInfoResponse.getBoardType()).isEqualTo(publishedBOARD.getBoardType().toString());
				softly.assertThat(boardInfoResponse.getStatus()).isEqualTo(publishedBOARD.getStatus().toString());
				softly.assertThat(boardInfoResponse.getView()).isEqualTo(publishedBOARD.getViewCount());
				softly.assertThat(boardInfoResponse.getLike()).isEqualTo(publishedBOARD.getLikes().size());
				softly.assertThat(boardInfoResponse.getCreatedAt()).isEqualTo(publishedBOARD.getCreatedAt());
				softly.assertThat(boardInfoResponse.getUpdatedAt()).isEqualTo(publishedBOARD.getUpdatedAt());
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
			SecurityContextHolder.getContext().setAuthentication(new JwtAuthentication(JwtUserClaim.create(savedUser)));

			// when
			BoardInfoResponse boardInfoResponse = boardService.searchSingleBoard(draftBoard.getId());

			// then
			assertSoftly(softly -> {
				softly.assertThat(boardInfoResponse.getBoardId()).isEqualTo(draftBoard.getId());
				softly.assertThat(boardInfoResponse.getTitle()).isEqualTo(draftBoard.getTitle());
				softly.assertThat(boardInfoResponse.getContents()).isEqualTo(draftBoard.getContent());
				softly.assertThat(boardInfoResponse.getBoardType()).isEqualTo(draftBoard.getBoardType().toString());
				softly.assertThat(boardInfoResponse.getStatus()).isEqualTo(draftBoard.getStatus().toString());
				softly.assertThat(boardInfoResponse.getView()).isEqualTo(draftBoard.getViewCount());
				softly.assertThat(boardInfoResponse.getLike()).isEqualTo(draftBoard.getLikes().size());
				softly.assertThat(boardInfoResponse.getCreatedAt()).isEqualTo(draftBoard.getCreatedAt());
				softly.assertThat(boardInfoResponse.getUpdatedAt()).isEqualTo(draftBoard.getUpdatedAt());
			});

			assertSoftly(softly -> {
				softly.assertThat(redisTemplate.opsForValue().get( BOARD_VIEW_KEY+ draftBoard.getId()))
					.isEqualTo("1");
				softly.assertThat(events.stream(BoardViewEvent.class).count()).isEqualTo(1);
			});
		}

		@Test
		void 실패_임시저장_게시물은_게시물_소유자만_조회할_수_있다() {
			// given
			Board draftBoard = testFixtureBuilder.buildBoard(DRAFT_SEMINAR_BOARD(savedUser));




			SecurityContextHolder.getContext()
				.setAuthentication(
					new JwtAuthentication(JwtUserClaim.create(testFixtureBuilder.buildUser(SEMINAR_WRITER_USER()))));


			// when -> then
			assertSoftly(softly -> {
				softly.assertThatThrownBy(() -> boardService.searchSingleBoard(draftBoard.getId()))
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
				softly.assertThatThrownBy(() -> boardService.searchSingleBoard(draftBoard.getId() + 1))
					.isInstanceOf(ServiceException.class)
					.hasFieldOrPropertyWithValue("errorCode", ErrorCode.BOARD_NOT_FOUND);
				softly.assertThat(redisTemplate.opsForValue().get(BOARD_VIEW_KEY + draftBoard.getId()))
					.isNull();
				softly.assertThat(events.stream(BoardViewEvent.class).count()).isEqualTo(0);
			});

		}
	}

}
