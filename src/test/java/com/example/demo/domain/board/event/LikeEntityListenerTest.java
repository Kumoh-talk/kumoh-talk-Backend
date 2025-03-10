package com.example.demo.domain.board.event;

import com.example.demo.builder.BuilderSupporter;
import com.example.demo.builder.TestFixtureBuilder;
import com.example.demo.infra.board.entity.Like;
import com.example.demo.domain.board.service.service.BoardService;
import com.example.demo.domain.board.service.service.LikeService;
import com.example.demo.domain.comment.TransactionalTask;
import com.example.demo.domain.notification.domain.entity.Notification;
import com.example.demo.domain.notification.repository.NotificationRepository;
import com.example.demo.domain.user.domain.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import static com.example.demo.fixture.notification.NotificationFixtures.LIKE_NOTIFICATION;
import static com.example.demo.fixture.notification.NotificationUserFixtures.NOTIFICATION_USER;
import static com.example.demo.fixture.user.UserFixtures.ADMIN_USER;
import static com.example.demo.fixture.user.UserFixtures.SEMINAR_WRITER_USER;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@Import(value = {TestFixtureBuilder.class, BuilderSupporter.class, TransactionalTask.class})
class LikeEntityListenerTest {
    @Autowired
    private TestFixtureBuilder testFixtureBuilder;
    @Autowired
    private TransactionalTask transactionalTask;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private LikeService likeService;
    @Autowired
    private BoardService boardService;


    @Nested
    @DisplayName("좋아요 삭제 시 연관 알림 삭제 통합 테스트")
    class deleteLike {
        private final User boardWriterUser = SEMINAR_WRITER_USER();
        private final User likeUser = ADMIN_USER();

        private Like savedLike;
        private Notification savedNotification;

        @BeforeEach
        void setUp() {
            savedLike = transactionalTask.createBoardAndLike(boardWriterUser, likeUser);

            Notification notification = LIKE_NOTIFICATION(savedLike);
            notification.getNotificationUserList().add(NOTIFICATION_USER(notification, savedLike.getBoard().getUser()));
            savedNotification = testFixtureBuilder.buildNotification(notification);
        }

        @AfterEach
        void tearDown() {
            transactionalTask.cleanUp();
        }

        @Test
        @Disabled //TODO : 해당 리팩터링 이후에 Disabled 해제
        void 성공_좋아요_삭제_시_연관_알림도_삭제된다() {
            // given
            assertTrue(notificationRepository.existsById(savedNotification.getId()));

            // when
            likeService.unlikeBoard(savedLike.getUser().getId(), savedLike.getBoard().getId());

            // then
            Awaitility.await().untilAsserted(() -> {
                assertFalse(notificationRepository.existsById(savedNotification.getId()));
            });
        }

        @Test
        @Disabled //TODO : 해당 리팩터링 이후에 Disabled 해제
        void 성공_마크다운_게시물_삭제_시_연관_좋아요의_알림도_삭제된다() {
            // given
            assertTrue(notificationRepository.existsById(savedNotification.getId()));

            // when
            boardService.deleteBoard(savedLike.getBoard().getUser().getId(), savedLike.getBoard().getId());

            // then
            Awaitility.await().untilAsserted(() -> {
                assertFalse(notificationRepository.existsById(savedNotification.getId()));
            });
        }
    }

}
