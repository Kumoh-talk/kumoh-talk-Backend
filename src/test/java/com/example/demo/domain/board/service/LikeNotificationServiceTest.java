package com.example.demo.domain.board.service;

import com.example.demo.builder.TestFixtureBuilder;
import com.example.demo.infra.board.entity.Like;
import com.example.demo.domain.board.service.service.LikeNotificationService;
import com.example.demo.domain.comment.TransactionalTask;
import com.example.demo.domain.notification.domain.entity.Notification;
import com.example.demo.domain.notification.domain.entity.NotificationUser;
import com.example.demo.domain.notification.domain.vo.NotificationType;
import com.example.demo.domain.user.domain.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.example.demo.fixture.user.UserFixtures.ADMIN_USER;
import static com.example.demo.fixture.user.UserFixtures.SEMINAR_WRITER_USER;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SpringBootTest
@ActiveProfiles("test")
@Import(value = {TestFixtureBuilder.class, TransactionalTask.class})
public class LikeNotificationServiceTest {
    @Autowired
    private TransactionalTask transactionalTask;

    @Autowired
    private LikeNotificationService likeNotificationService;

    @Nested
    @DisplayName("<좋아요 생성 시 연관 알림 생성 통합 테스트>")
    class saveLike {
        private final User boardWriterUser = SEMINAR_WRITER_USER();
        private final User likeUser = ADMIN_USER();

        private Like savedLike;

        @BeforeEach
        void setUp() {
            savedLike = transactionalTask.createBoardAndLike(boardWriterUser, likeUser);
        }

        @AfterEach
        void tearDown() {
            transactionalTask.cleanUp();
        }

        @Test
        void 성공_좋아요_저장_시_알림도_저장된다() {
            // when
            CompletableFuture<Notification> completableFuture = likeNotificationService.saveLikeNotification(savedLike);

            // then
            Notification notification = completableFuture.join();

            assertSoftly(softly -> {
                softly.assertThat(notification.getInvokerId()).isEqualTo(savedLike.getId());
                softly.assertThat(notification.getInvokerType()).isEqualTo(NotificationType.BOARD_LIKE);
                softly.assertThat(notification.getBoardId()).isEqualTo(savedLike.getBoard().getId());
                softly.assertThat(notification.getSenderNickname()).isEqualTo(savedLike.getUser().getNickname());

                List<NotificationUser> notificationUserList = notification.getNotificationUserList();
                softly.assertThat(notificationUserList).isNotEmpty();
                softly.assertThat(notificationUserList.get(0).getUser().getId()).isEqualTo(savedLike.getBoard().getUser().getId());
                softly.assertThat(notificationUserList.size()).isEqualTo(1);
            });
        }
    }
}
