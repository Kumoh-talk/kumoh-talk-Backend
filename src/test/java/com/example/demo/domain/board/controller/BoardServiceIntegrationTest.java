package com.example.demo.domain.board.controller;

import static com.example.demo.fixture.board.BoardDomainFixtures.*;
import static com.example.demo.infra.fixture.board.BoardFixtures.*;
import static com.example.demo.fixture.user.UserFixtures.*;
import static org.assertj.core.api.SoftAssertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.example.demo.application.board.dto.vo.Status;
import com.example.demo.base.IntegrationTest;
import com.example.demo.domain.board.service.entity.BoardCategoryNames;
import com.example.demo.domain.board.service.entity.BoardContent;
import com.example.demo.domain.board.service.entity.BoardInfo;
import com.example.demo.domain.newsletter.event.EmailNotificationEvent;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.UserTarget;
import com.example.demo.fixture.board.BoardDomainFixtures;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.domain.board.service.view.BoardViewEvent;
import com.example.demo.infra.board.category.entity.Category;
import com.example.demo.infra.board.entity.Board;

public class BoardServiceIntegrationTest extends IntegrationTest {


	@Nested
	@DisplayName("<세미나/공지사항 게시물 작성>")
	class Describe_saveBoard {
		private User savedUser;

		@BeforeEach
		void setUp() {
			savedUser = testFixtureBuilder.buildUser(ADMIN_USER());
		}

		@Test
		void 성공_게시물_작성이_성공한다() {
			// given
			BoardContent boardContent = BOARD_SEMINAR_DRAFT_CONTENT;
			BoardCategoryNames boardCategoryNames = BOARD_CATEGORY_NAMES;

			// when
			BoardInfo draftBoard = boardService.saveDraftBoard(savedUser.getId(), boardContent, boardCategoryNames);

			// then
			assertSoftly(softly -> {
				softly.assertThat(draftBoard.getBoardContent().getTitle()).isEqualTo(boardContent.getTitle());
				softly.assertThat(draftBoard.getBoardContent().getContents()).isEqualTo(boardContent.getContents());
				softly.assertThat(draftBoard.getBoardContent().getBoardType()).isEqualTo(boardContent.getBoardType());
				softly.assertThat(draftBoard.getBoardContent().getBoardStatus())
					.isEqualTo(boardContent.getBoardStatus());
				softly.assertThat(draftBoard.getBoardCategoryNames().getCategories())
					.isEqualTo(boardCategoryNames.getCategories());
			});
		}

		@Test
		void 실패_공지사항_게시물은_관리자가_아니면_실패한다() {
			// given
			BoardContent boardContent = BOARD_NOTICE_DRAFT_CONTENT;
			BoardCategoryNames boardCategoryNames = BOARD_CATEGORY_NAMES;
			User failUser = testFixtureBuilder.buildUser(SEMINAR_WRITER_USER());

			// when -> then
			assertSoftly(softly -> {
				softly.assertThatThrownBy(() -> boardService.saveDraftBoard(failUser.getId(), boardContent, boardCategoryNames))
					.isInstanceOf(ServiceException.class)
					.hasFieldOrPropertyWithValue("errorCode", ErrorCode.NOT_AUTHORIZED_WRITE_NOTICE);
			});
		}

		@Test
		void 실패_게시물_작성은_유효하지_않은_유저면_실패한다() {
			// given
			BoardContent boardContent = BOARD_NOTICE_DRAFT_CONTENT;
			BoardCategoryNames boardCategoryNames = BOARD_CATEGORY_NAMES;

			// when -> then
			assertSoftly(softly -> {
				softly.assertThatThrownBy(() -> boardService.saveDraftBoard(999L, boardContent, boardCategoryNames))
					.isInstanceOf(ServiceException.class)
					.hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_NOT_FOUND);
			});
		}
	}


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
		void 성공_조회수캐싱_ApplicationEvent_데이터를_조회할수있다() {
			// given
			Board publishedBOARD = jpaTestFixtureBuilder.buildBoard(PUBLISHED_SEMINAR_BOARD(savedUser));
			List<String> categoryNames = List.of("category1", "category2");
			List<Category> categories = jpaTestFixtureBuilder.buildCategories(categoryNames);
			jpaTestFixtureBuilder.buildBoardCategories(publishedBOARD, categories);

			// when
			BoardInfo boardInfo = boardService.searchSingleBoard(null,publishedBOARD.getId());
			BoardContent boardContent = boardInfo.getBoardContent();
			BoardCategoryNames boardCategoryNames = boardInfo.getBoardCategoryNames();

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

				softly.assertThat(boardCategoryNames.getCategories().size()).isEqualTo(categoryNames.size());
				boardCategoryNames.getCategories().forEach(categoryName -> {
					softly.assertThat(categoryNames).contains(categoryName);
				});
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
			List<String> categoryNames = List.of("category1", "category2");
			List<Category> categories = jpaTestFixtureBuilder.buildCategories(categoryNames);
			jpaTestFixtureBuilder.buildBoardCategories(draftBoard, categories);

			// when
			BoardInfo boardInfo = boardService.searchSingleBoard(savedUser.getId(),draftBoard.getId());
			BoardContent boardContent = boardInfo.getBoardContent();
			UserTarget userTarget = boardInfo.getUserTarget();
			BoardCategoryNames boardCategoryNames = boardInfo.getBoardCategoryNames();
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

				softly.assertThat(boardCategoryNames.getCategories().size()).isEqualTo(categoryNames.size());
				boardCategoryNames.getCategories().forEach(categoryName -> {
					softly.assertThat(categoryNames).contains(categoryName);
				});
			});

			assertSoftly(softly -> {
				softly.assertThat(redisTemplate.opsForValue().get( BOARD_VIEW_KEY+ draftBoard.getId()))
					.isEqualTo("1");
				softly.assertThat(events.stream(BoardViewEvent.class).count()).isEqualTo(1);
			});
		}

		@Test
		void 실패_임시저장_게시물_조회시_비인증_회원은_실패한다() {
			// given
			Board draftBoard = testFixtureBuilder.buildBoard(DRAFT_SEMINAR_BOARD(savedUser));

			// when -> then
			assertSoftly(softly -> {
				softly.assertThatThrownBy(() -> boardService.searchSingleBoard(null,draftBoard.getId()))
					.isInstanceOf(ServiceException.class)
					.hasFieldOrPropertyWithValue("errorCode", ErrorCode.NOT_ACCESS_USER);
				softly.assertThat(redisTemplate.opsForValue().get(BOARD_VIEW_KEY + draftBoard.getId()))
					.isNull();
				softly.assertThat(events.stream(BoardViewEvent.class).count()).isEqualTo(0);
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

	@Nested
	@DisplayName("<세미나/공지사항 게시물 수정>")
	class updateBoard{

		private User savedUser;

		@BeforeEach
		void setUp() {
			savedUser = testFixtureBuilder.buildUser(ADMIN_USER());
		}

		@Test
		void 성공_게시물_수정이_성공한다() {
			// given
			Board draftBoard = testFixtureBuilder.buildBoard(DRAFT_SEMINAR_BOARD(savedUser));
			List<String> categoryNames = List.of("category1", "category2");
			List<Category> categories = jpaTestFixtureBuilder.buildCategories(categoryNames);
			jpaTestFixtureBuilder.buildBoardCategories(draftBoard, categories);

			BoardContent updateBoardContent = UPDATE_BOARD_CONTENT;
			List<String> updateCategoryNames = List.of("updateCategory1", "updateCategory2","category1");
			// when
			boardService.updateBoard(savedUser.getId(), draftBoard.getId(), updateBoardContent,
				new BoardCategoryNames(updateCategoryNames), false);
			BoardInfo boardInfo = boardService.searchSingleBoard(savedUser.getId(),draftBoard.getId());

			// then
			assertSoftly(softly -> {
				// 게시물 정보는 성공적으로 수정된다.
				softly.assertThat(boardInfo.getBoardContent().getTitle()).isEqualTo(updateBoardContent.getTitle());
				softly.assertThat(boardInfo.getBoardContent().getContents()).isEqualTo(updateBoardContent.getContents());
				softly.assertThat(boardInfo.getBoardContent().getBoardType()).isEqualTo(draftBoard.getBoardType());
				softly.assertThat(boardInfo.getBoardContent().getBoardStatus()).isEqualTo(Status.DRAFT);
				// 게시물 작성자 정보는 유진된다.
				softly.assertThat(boardInfo.getUserTarget().getUserId()).isEqualTo(savedUser.getId());
				softly.assertThat(boardInfo.getUserTarget().getNickName()).isEqualTo(savedUser.getNickname());
				softly.assertThat(boardInfo.getUserTarget().getUserRole()).isEqualTo(savedUser.getRole());
				// 연관 카테고리도 성공적으로 수정된다.
				softly.assertThat(boardInfo.getBoardCategoryNames().getCategories().size()).isEqualTo(updateCategoryNames.size());
				boardInfo.getBoardCategoryNames().getCategories().forEach(categoryName -> {
					softly.assertThat(updateCategoryNames).contains(categoryName);
				});
				// 이메일 이벤트가 발생하지 않는다.
				softly.assertThat(events.stream(EmailNotificationEvent.class).count()).isEqualTo(0);
			});
		}

		@Test
		void 성공_세미나_게시물_상태가_게시로_변경되면_이메일_이벤트가_발생한다() {
			// given
			Board draftBoard = testFixtureBuilder.buildBoard(DRAFT_SEMINAR_BOARD(savedUser));
			List<String> categoryNames = List.of("category1", "category2");
			List<Category> categories = jpaTestFixtureBuilder.buildCategories(categoryNames);
			jpaTestFixtureBuilder.buildBoardCategories(draftBoard, categories);

			BoardContent updateBoardContent = UPDATE_BOARD_CONTENT;
			List<String> updateCategoryNames = List.of("updateCategory1", "updateCategory2","category1");
			// when
			boardService.updateBoard(savedUser.getId(), draftBoard.getId(), updateBoardContent,
				new BoardCategoryNames(updateCategoryNames), true);
			BoardInfo boardInfo = boardService.searchSingleBoard(savedUser.getId(),draftBoard.getId());

			// then
			assertSoftly(softly -> {
				// 게시물 정보는 성공적으로 수정된다.
				softly.assertThat(boardInfo.getBoardContent().getTitle()).isEqualTo(updateBoardContent.getTitle());
				softly.assertThat(boardInfo.getBoardContent().getContents()).isEqualTo(updateBoardContent.getContents());
				softly.assertThat(boardInfo.getBoardContent().getBoardType()).isEqualTo(draftBoard.getBoardType());
				softly.assertThat(boardInfo.getBoardContent().getBoardStatus()).isEqualTo(Status.PUBLISHED);
				// 게시물 작성자 정보는 유진된다.
				softly.assertThat(boardInfo.getUserTarget().getUserId()).isEqualTo(savedUser.getId());
				softly.assertThat(boardInfo.getUserTarget().getNickName()).isEqualTo(savedUser.getNickname());
				softly.assertThat(boardInfo.getUserTarget().getUserRole()).isEqualTo(savedUser.getRole());
				// 연관 카테고리도 성공적으로 수정된다.
				softly.assertThat(boardInfo.getBoardCategoryNames().getCategories().size()).isEqualTo(updateCategoryNames.size());
				boardInfo.getBoardCategoryNames().getCategories().forEach(categoryName -> {
					softly.assertThat(updateCategoryNames).contains(categoryName);
				});
				// 이메일 이벤트가 발생한다.
				softly.assertThat(events.stream(EmailNotificationEvent.class).count()).isEqualTo(1);
			});
		}

		@Test
		void 실패_게시물_수정은_게시물_작성자가_아니면_실패한다() {
			// given
			Board draftBoard = testFixtureBuilder.buildBoard(DRAFT_SEMINAR_BOARD(savedUser));
			List<String> categoryNames = List.of("category1", "category2");
			List<Category> categories = jpaTestFixtureBuilder.buildCategories(categoryNames);
			jpaTestFixtureBuilder.buildBoardCategories(draftBoard, categories);

			BoardContent updateBoardContent = UPDATE_BOARD_CONTENT;
			List<String> updateCategoryNames = List.of("updateCategory1", "updateCategory2","category1");

			// when -> then
			assertSoftly(softly -> {
				softly.assertThatThrownBy(() -> boardService.updateBoard(9999L, draftBoard.getId(), updateBoardContent,
					new BoardCategoryNames(updateCategoryNames), false))
					.isInstanceOf(ServiceException.class)
					.hasFieldOrPropertyWithValue("errorCode", ErrorCode.NOT_ACCESS_USER);
				softly.assertThat(events.stream(EmailNotificationEvent.class).count()).isEqualTo(0);
			});
		}

		@Test
		void 실패_게시물_수정은_유효하지_않은_게시물이면_실패한다() {
			// given
			BoardContent updateBoardContent = UPDATE_BOARD_CONTENT;
			List<String> updateCategoryNames = List.of("updateCategory1", "updateCategory2","category1");

			// when -> then
			assertSoftly(softly -> {
				softly.assertThatThrownBy(() -> boardService.updateBoard(savedUser.getId(), 999L, updateBoardContent,
					new BoardCategoryNames(updateCategoryNames), false))
					.isInstanceOf(ServiceException.class)
					.hasFieldOrPropertyWithValue("errorCode", ErrorCode.BOARD_NOT_FOUND);
				softly.assertThat(events.stream(EmailNotificationEvent.class).count()).isEqualTo(0);
			});
		}
	}

	@Nested
	@DisplayName("<세미나/공지사항 게시물 삭제>")
	class deleteBoardTest{
		private User savedUser;

		@BeforeEach
		void setUp() {
			savedUser = testFixtureBuilder.buildUser(ADMIN_USER());
		}

		@Test
		void  성공_게시물_삭제가_성공한다() {
			// given
			Board draftBoard = testFixtureBuilder.buildBoard(DRAFT_SEMINAR_BOARD(savedUser));
			List<String> categoryNames = List.of("category1", "category2");
			List<Category> categories = jpaTestFixtureBuilder.buildCategories(categoryNames);
			jpaTestFixtureBuilder.buildBoardCategories(draftBoard, categories);

			// when
			boardService.deleteBoard(savedUser.getId(), draftBoard.getId());

			// then
			assertSoftly(softly -> {
				softly.assertThatThrownBy(() -> boardService.searchSingleBoard(savedUser.getId(), draftBoard.getId()))
					.isInstanceOf(ServiceException.class)
					.hasFieldOrPropertyWithValue("errorCode", ErrorCode.BOARD_NOT_FOUND);
			});
		}

		@Test
		void 실패_게시물_삭제는_게시물_작성자가_아니면_실패한다() {
			// given
			Board draftBoard = testFixtureBuilder.buildBoard(DRAFT_SEMINAR_BOARD(savedUser));

			// when -> then
			assertSoftly(softly -> {
				softly.assertThatThrownBy(() -> boardService.deleteBoard(999L, draftBoard.getId()))
					.isInstanceOf(ServiceException.class)
					.hasFieldOrPropertyWithValue("errorCode", ErrorCode.NOT_ACCESS_USER);
			});
		}

		@Test
		void 실패_게시물_삭제는_유효하지_않은_게시물이면_실패한다() {
			// given
			Board draftBoard = testFixtureBuilder.buildBoard(DRAFT_SEMINAR_BOARD(savedUser));

			// when -> then
			assertSoftly(softly -> {
				softly.assertThatThrownBy(() -> boardService.deleteBoard(savedUser.getId(), draftBoard.getId() + 1))
					.isInstanceOf(ServiceException.class)
					.hasFieldOrPropertyWithValue("errorCode", ErrorCode.BOARD_NOT_FOUND);
			});
		}
	}

}
