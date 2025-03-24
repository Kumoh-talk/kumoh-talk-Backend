package com.example.demo.infra.board.repository;

import static com.example.demo.fixture.user.UserFixtures.*;
import static com.example.demo.infra.fixture.board.BoardFixtures.*;
import static org.assertj.core.api.SoftAssertions.*;

import java.util.Optional;

import com.example.demo.infra.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.base.RepositoryTest;
import com.example.demo.infra.board.entity.Board;

public class BoardJpaRepositoryTest extends RepositoryTest {

	@Autowired
	private BoardJpaRepository boardJpaRepository;


	@Nested
	@DisplayName("[findBoardAndUserAndCategory] 단일 게시글 게시글, 카테고리, 게시글 유저 fetch join 조회 테스트")
	class findBoardInfoTest{
		@Test
		void 성공_카테고리가없어도_게시글과유저는_성공적으로_조회_가능하다() {
			// given
			User savedUser = testFixtureBuilder.buildUser(ADMIN_USER());
			Board publishedBOARD = testFixtureBuilder.buildBoard(PUBLISHED_SEMINAR_BOARD(savedUser));

			// when
			Optional<Board> board = boardJpaRepository.findBoardAndUserAndCategory(
				publishedBOARD.getId());

			// then
			assertSoftly(softly -> {
				softly.assertThat(board).isPresent();
				softly.assertThat(board.get().getId()).isEqualTo(publishedBOARD.getId());
				softly.assertThat(board.get().getTitle()).isEqualTo(publishedBOARD.getTitle());
				softly.assertThat(board.get().getUser().getId()).isEqualTo(savedUser.getId());
			});
		}
	}
}
