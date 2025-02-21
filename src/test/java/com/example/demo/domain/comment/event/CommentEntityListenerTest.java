package com.example.demo.domain.comment.event;

import com.example.demo.builder.BuilderSupporter;
import com.example.demo.builder.TestFixtureBuilder;
import com.example.demo.domain.board.service.usecase.BoardUseCase;
import com.example.demo.domain.comment.TransactionalTask;
import com.example.demo.domain.comment.domain.entity.BoardComment;
import com.example.demo.domain.comment.domain.entity.RecruitmentBoardComment;
import com.example.demo.domain.comment.service.AbstractCommentService;
import com.example.demo.domain.notification.domain.entity.Notification;
import com.example.demo.domain.notification.repository.NotificationRepository;
import com.example.demo.domain.recruitment_board.service.RecruitmentBoardService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import static com.example.demo.fixture.notification.NotificationFixtures.BOARD_COMMENT_NOTIFICATION;
import static com.example.demo.fixture.notification.NotificationFixtures.RECRUITMENT_BOARD_COMMENT_NOTIFICATION;
import static com.example.demo.fixture.notification.NotificationUserFixtures.NOTIFICATION_USER;
import static com.example.demo.fixture.user.UserFixtures.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@Import(value = {TestFixtureBuilder.class, BuilderSupporter.class, TransactionalTask.class})
class CommentEntityListenerTest {
    @Autowired
    private TestFixtureBuilder testFixtureBuilder;
    @Autowired
    private TransactionalTask transactionalTask;

    @Autowired
    private NotificationRepository notificationRepository;

    @Qualifier("boardCommentService")
    @Autowired
    private AbstractCommentService commentService;
    @Autowired
    private BoardUseCase boardUseCase;
    @Autowired
    private RecruitmentBoardService recruitmentBoardService;

    @Nested
    @DisplayName("<댓글 삭제 시 연관 알림 삭제 통합 테스트>")
    class deleteComment {

        @AfterEach
        void tearDown() {
            transactionalTask.cleanUp();
        }

        @Test
        void 성공_댓글_삭제_시_연관_알림도_삭제된다() {
            // given
            BoardComment savedComment = transactionalTask.createBoardAndComment(SEMINAR_WRITER_USER(), ADMIN_USER());

            Notification notification = BOARD_COMMENT_NOTIFICATION(savedComment);
            notification.getNotificationUserList().add(NOTIFICATION_USER(notification, savedComment.getBoard().getUser()));
            Notification savedNotification = testFixtureBuilder.buildNotification(notification);

            assertTrue(notificationRepository.existsById(savedNotification.getId()));

            // when
            commentService.deleteComment(savedComment.getUser().getId(), savedComment.getId(), false);

            // then
            Awaitility.await().untilAsserted(() -> {
                assertFalse(notificationRepository.existsById(savedNotification.getId()));
            });
        }

        @Test
        void 성공_마크다운_게시물_삭제_시_연관_댓글의_알림도_삭제된다() {
            // given
            BoardComment savedComment = transactionalTask.createBoardAndComment(SEMINAR_WRITER_USER(), ADMIN_USER());

            Notification notification = BOARD_COMMENT_NOTIFICATION(savedComment);
            notification.getNotificationUserList().add(NOTIFICATION_USER(notification, savedComment.getBoard().getUser()));
            Notification savedNotification = testFixtureBuilder.buildNotification(notification);

            assertTrue(notificationRepository.existsById(savedNotification.getId()));

            // when
            boardUseCase.deleteBoard(savedComment.getBoard().getUser().getId(), savedComment.getBoard().getId());

            // then
            Awaitility.await().untilAsserted(() -> {
                assertFalse(notificationRepository.existsById(savedNotification.getId()));
            });
        }


        @Test
        void 성공_모집_게시물_삭제_시_연관_댓글의_알림도_삭제된다() {
            // given
            RecruitmentBoardComment savedComment = transactionalTask.createRecruitmentBoardAndComment(ACTIVE_USER(), ADMIN_USER());

            Notification notification = RECRUITMENT_BOARD_COMMENT_NOTIFICATION(savedComment);
            notification.getNotificationUserList().add(NOTIFICATION_USER(notification, savedComment.getBoard().getUser()));
            Notification savedNotification = testFixtureBuilder.buildNotification(notification);

            assertTrue(notificationRepository.existsById(savedNotification.getId()));

            // when
            recruitmentBoardService.deleteBoardAndForm(savedComment.getBoard().getUser().getId(), savedComment.getBoard().getId(), false);

            // then
            Awaitility.await().untilAsserted(() -> {
                assertFalse(notificationRepository.existsById(savedNotification.getId()));
            });
        }
    }
}