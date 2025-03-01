//package com.example.demo.domain.comment.service;
//
//import com.example.demo.builder.BuilderSupporter;
//import com.example.demo.builder.TestFixtureBuilder;
//import com.example.demo.domain.comment.TransactionalTask;
//import com.example.demo.domain.comment.domain.entity.BoardComment;
//import com.example.demo.domain.comment.repository.BoardCommentRepository;
//import com.example.demo.domain.notification.domain.entity.Notification;
//import com.example.demo.domain.notification.domain.entity.NotificationUser;
//import com.example.demo.domain.notification.domain.vo.NotificationType;
//import com.example.demo.domain.user.domain.User;
//import com.example.demo.infra.comment.entity.BoardComment;
//import com.example.demo.infra.comment.repository.jpa.BoardCommentJpaRepository;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//
//import static com.example.demo.fixture.comment.CommentFixtures.BOARD_COMMENT;
//import static com.example.demo.fixture.user.UserFixtures.*;
//import static org.assertj.core.api.SoftAssertions.assertSoftly;
//
//@SpringBootTest
//@ActiveProfiles("test")
//@Import(value = {TestFixtureBuilder.class, BuilderSupporter.class, TransactionalTask.class})
//class CommentNotificationServiceTest {
//    @Autowired
//    private TestFixtureBuilder testFixtureBuilder;
//    @Autowired
//    private TransactionalTask transactionalTask;
//
//    @Autowired
//    private CommentNotificationService commentNotificationService;
//    @Autowired
//    private BoardCommentJpaRepository boardCommentRepository;
//
//    @Nested
//    @DisplayName("<댓글 생성 시 연관 알림 생성 통합 테스트>")
//    class saveComment {
//        private final NotificationType invokerType = NotificationType.BOARD_COMMENT;
//        private final User boardWriterUser = SEMINAR_WRITER_USER();
//        private final User CommentWriterUser1 = ADMIN_USER();
//        private final User CommentWriterUser2 = ACTIVE_USER();
//        private final User CommentWriterUser3 = USER();
//
//        private BoardComment savedBoardComment1;
//        private BoardComment savedBoardComment2;
//
//        @BeforeEach
//        void setUp() {
//            // 게시물 및 부모 댓글 생성
//            savedBoardComment1 = transactionalTask.createBoardAndComment(boardWriterUser, CommentWriterUser1);
//
//            // 대댓글 생성
//            User savedCommentWriterUser2 = testFixtureBuilder.buildUser(CommentWriterUser2);
//            savedBoardComment2 = testFixtureBuilder.buildBoardComment(BOARD_COMMENT(savedBoardComment1.getBoard(), savedCommentWriterUser2, savedBoardComment1));
//        }
//
//        @AfterEach
//        void tearDown() {
//            transactionalTask.cleanUp();
//        }
//
//        @Test
//        void 성공_댓글_저장_시_알림도_저장된다() {
//            // given
//            User savedCommentWriterUser3 = testFixtureBuilder.buildUser(CommentWriterUser3);
//            BoardComment boardComment3 = testFixtureBuilder.buildBoardComment(BOARD_COMMENT(savedBoardComment1.getBoard(), savedCommentWriterUser3, null));
//
//            List<User> testNotificationUserList = new ArrayList<>();
//            testNotificationUserList.add(boardComment3.getBoard().getUser());
//            testNotificationUserList.add(savedBoardComment1.getUser());
//
//            // when
//            CompletableFuture<Notification> completableFuture = commentNotificationService.saveCommentNotification(boardComment3, invokerType, boardCommentRepository);
//
//            // then
//            Notification notification = completableFuture.join();
//
//            assertSoftly(softly -> {
//                softly.assertThat(notification.getInvokerId()).isEqualTo(boardComment3.getId());
//                softly.assertThat(notification.getInvokerType()).isEqualTo(invokerType);
//                softly.assertThat(notification.getBoardId()).isEqualTo(boardComment3.getBoard().getId());
//                softly.assertThat(notification.getSenderNickname()).isEqualTo(boardComment3.getUser().getNickname());
//
//                List<NotificationUser> notificationUserList = notification.getNotificationUserList();
//                softly.assertThat(notificationUserList).isNotEmpty();
//
//                for (User user : testNotificationUserList) {
//                    softly.assertThat(notificationUserList.stream()
//                                    .anyMatch(notificationUser -> notificationUser.getUser().getId().equals(user.getId())))
//                            .isTrue();
//                }
//                softly.assertThat(notificationUserList.size()).isEqualTo(testNotificationUserList.size());
//            });
//        }
//
//        @Test
//        void 성공_대댓글_저장_시_알림도_저장된다() {
//            // given
//            User savedCommentWriterUser3 = testFixtureBuilder.buildUser(CommentWriterUser3);
//            BoardComment boardComment3 = testFixtureBuilder.buildBoardComment(BOARD_COMMENT(savedBoardComment1.getBoard(), savedCommentWriterUser3, savedBoardComment1));
//
//            List<User> testNotificationUserList = new ArrayList<>();
//            testNotificationUserList.add(boardComment3.getBoard().getUser());
//            testNotificationUserList.add(savedBoardComment1.getUser());
//            testNotificationUserList.add(savedBoardComment2.getUser());
//
//            // when
//            CompletableFuture<Notification> completableFuture = commentNotificationService.saveCommentNotification(boardComment3, invokerType, boardCommentRepository);
//
//            // then
//            Notification notification = completableFuture.join();
//
//            assertSoftly(softly -> {
//                softly.assertThat(notification.getInvokerId()).isEqualTo(boardComment3.getId());
//                softly.assertThat(notification.getInvokerType()).isEqualTo(invokerType);
//                softly.assertThat(notification.getBoardId()).isEqualTo(boardComment3.getBoard().getId());
//                softly.assertThat(notification.getSenderNickname()).isEqualTo(boardComment3.getUser().getNickname());
//
//                List<NotificationUser> notificationUserList = notification.getNotificationUserList();
//                softly.assertThat(notificationUserList).isNotEmpty();
//
//                for (User user : testNotificationUserList) {
//                    softly.assertThat(notificationUserList.stream()
//                                    .anyMatch(notificationUser -> notificationUser.getUser().getId().equals(user.getId())))
//                            .isTrue();
//                }
//                softly.assertThat(notificationUserList.size()).isEqualTo(testNotificationUserList.size());
//            });
//        }
//    }
//
//
//}