package com.example.demo.application.board.integration;

import static com.example.demo.fixture.user.UserFixtures.*;
import static com.example.demo.infra.fixture.board.BoardFixtures.*;
import static org.assertj.core.api.SoftAssertions.*;

import java.util.List;

import com.example.demo.infra.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.example.demo.base.IntegrationTest;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.infra.board.category.entity.Category;
import com.example.demo.infra.board.entity.Board;

public class BoardAdminServiceIntegrationTest extends IntegrationTest {

	@Nested
	@DisplayName("<관리자용 게시글 삭제 테스트>")
	class deleteBoard {
		private User savedUser;

		private static final String BOARD_VIEW_KEY = "board:view:";

		@BeforeEach
		void setUp() {
			savedUser = testFixtureBuilder.buildUser(SEMINAR_WRITER_USER());
		}

		@Test
		void 성공_타인의_게시글을_성공적으로_삭제한다() {
			// given
			Board publishedBOARD = jpaTestFixtureBuilder.buildBoard(PUBLISHED_SEMINAR_BOARD(savedUser));
			List<String> categoryNames = List.of("category1", "category2");
			List<Category> categories = jpaTestFixtureBuilder.buildCategories(categoryNames);
			jpaTestFixtureBuilder.buildBoardCategories(publishedBOARD, categories);

			// when
			boardAdminService.deleteBoard(publishedBOARD.getId());

			// then
			assertSoftly(softly -> {
				softly.assertThatThrownBy(() -> boardService.searchSingleBoard(savedUser.getId(), publishedBOARD.getId()))
					.isInstanceOf(ServiceException.class)
					.hasFieldOrPropertyWithValue("errorCode", ErrorCode.BOARD_NOT_FOUND);
			});
		}

		@Test
		void 실패_유효하지않는_게시물은_삭제에_실패한다() {
			// given -> when -> then
			assertSoftly(softly -> {
				softly.assertThatThrownBy(() -> boardAdminService.deleteBoard(1L))
					.isInstanceOf(ServiceException.class)
					.hasFieldOrPropertyWithValue("errorCode", ErrorCode.BOARD_NOT_FOUND);
			});
		}


	}
}
