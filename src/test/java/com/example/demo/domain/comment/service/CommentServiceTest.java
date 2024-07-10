// package com.example.demo.domain.comment.service;
//
// import com.example.demo.domain.board.Repository.BoardRepository;
// import com.example.demo.domain.board.domain.entity.Board;
// import com.example.demo.domain.comment.domain.entity.Comment;
// import com.example.demo.domain.comment.domain.response.CommentInfo;
// import com.example.demo.domain.comment.domain.response.CommentResponse;
// import com.example.demo.domain.comment.repository.CommentRepository;
// import com.example.demo.domain.user.domain.User;
// import com.example.demo.domain.user.domain.vo.Status;
// import com.example.demo.domain.user.repository.UserRepository;
// import com.example.demo.global.base.exception.ServiceException;
// import org.junit.jupiter.api.*;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.BDDMockito;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.test.util.ReflectionTestUtils;
//
// import java.time.LocalDateTime;
// import java.util.ArrayList;
// import java.util.Collections;
// import java.util.List;
// import java.util.Optional;
//
// import static com.example.demo.domain.user.domain.vo.Role.ROLE_USER;
// import static org.assertj.core.api.Assertions.assertThat;
// import static org.junit.jupiter.api.Assertions.assertThrows;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.eq;
//
// @ExtendWith(MockitoExtension.class)
// public class CommentServiceTest {
//     @InjectMocks
//     private CommentService commentService;
//     @Mock
//     private CommentRepository commentRepository;
//     @Mock
//     private BoardRepository boardRepository;
//     @Mock
//     private UserRepository userRepository;
//
//     private Long boardId = 1L;
//     private Board board;
//     private List<Comment> commentList;
//     @BeforeEach
//     void setUp(){
//         board = Board.builder()
//                 .id(boardId)
//                 .title("제목")
//                 .content("내용").build();
//         commentList = TestDataFactory.createComments(board);
//     }
//     @AfterEach
//     void clear(){
//         board = null;   // 가비지 컬렉션 유도
//         commentList.clear();
//     }
//
//     @Nested
//     @DisplayName("게시물 댓글 조회 서비스")
//     class GetComments{
//         @Test
//         @DisplayName("댓글 반환 성공")
//         public void success(){
//             // given
//             BDDMockito.given(boardRepository.findPostByIdWithComments(any(Long.class))).willReturn(Optional.ofNullable(board));
//             BDDMockito.given(commentRepository.findByBoard_idOrderByCreatedAtAsc(any(Long.class))).willReturn(commentList);
//             // when
//             CommentResponse commentResponse = commentService.findByBoardId(boardId);
//             // then
//             List<CommentInfo> commentInfoList = commentResponse.getCommentInfoList();
//
//             assertThat(commentResponse.getCommentsCount()).isEqualTo(commentList.size());
//             assertThat(commentInfoList.get(0).getCommentId()).isEqualTo(commentList.get(0).getId());
//             assertThat(commentInfoList.get(0).getGroupId()).isEqualTo(null);
//             assertThat(commentInfoList.get(0).getUserNickname()).isEqualTo(commentList.get(0).getUser().getNickname());
//             assertThat(commentInfoList.get(0).getContents()).isEqualTo(commentList.get(0).getContent());
//             assertThat(commentInfoList.get(0).getLikedCount()).isEqualTo(commentList.get(0).getLikedUsers().size());
//             assertThat(commentInfoList.get(0).getReplyComments().get(0).getCommentId()).isEqualTo(2L);
//             assertThat(commentInfoList.get(0).getCreatedAt()).isEqualTo(commentList.get(0).getCreatedAt());
//             assertThat(commentInfoList.get(0).getUpdatedAt()).isEqualTo(commentList.get(0).getUpdatedAt());
//             assertThat(commentInfoList.get(0).getDeletedAt()).isEqualTo(commentList.get(0).getDeletedAt());
//         }
//
//         @Test
//         @DisplayName("댓글이 존재하지 않는 게시물은 빈 리스트 반환")
//         public void commentsEmptyCase(){
//             // given
//             BDDMockito.given(boardRepository.findPostByIdWithComments(any(Long.class))).willReturn(Optional.ofNullable(board));
//             BDDMockito.given(commentRepository.findByBoard_idOrderByCreatedAtAsc(any(Long.class))).willReturn(Collections.emptyList());
//             // when
//             CommentResponse commentResponse = commentService.findByBoardId(boardId);
//             // then
//             assertThat(commentResponse.getCommentsCount()).isEqualTo(0);
//         }
//
//         @Test
//         @DisplayName("존재하지 않는 게시물로 인한 실패")
//         public void notFoundBoardFail(){
//             // given
//             BDDMockito.given(boardRepository.findPostByIdWithComments(any(Long.class))).willReturn(Optional.empty());
//             // when -> then
//             assertThrows(ServiceException.class, () -> {commentService.findByBoardId(boardId);});
//         }
//     }
//
//     static class TestDataFactory{
//         public static User createUser(){
//             return User.builder()
//                     .id(1L)
//                     .name("a")
//                     .nickname("aNick")
//                     .email("a@")
//                     .password("aPassword")
//                     .role(ROLE_USER)
//                     .department("컴퓨터공학과")
//                     .status(Status.ATTENDING)
//                     .field("프론트엔드").build();
//         }
//         public static List<Comment> createComments(Board board){
//             List<Comment> commentList = new ArrayList<>();
//             User user = createUser();
//
//             Comment comment1 = Comment.builder()
//                     .content("aaa")
//                     .board(board)
//                     .user(user)
//                     .parentComment(null).build();
//             ReflectionTestUtils.setField(comment1, "id", 1L);
//             ReflectionTestUtils.setField(comment1, "createdAt", LocalDateTime.now());
//             ReflectionTestUtils.setField(comment1, "updatedAt", null);
//             ReflectionTestUtils.setField(comment1, "deletedAt", null);
//             comment1.getLikedUsers().add(user);
//
//
//             Comment comment2 = Comment.builder()
//                     .content("bbb")
//                     .board(board)
//                     .user(user)
//                     .parentComment(comment1).build();
//             ReflectionTestUtils.setField(comment2, "id", 2L);
//             ReflectionTestUtils.setField(comment1, "createdAt", LocalDateTime.now());
//             ReflectionTestUtils.setField(comment1, "updatedAt", null);
//             ReflectionTestUtils.setField(comment1, "deletedAt", null);
//
//             Comment comment3 = Comment.builder()
//                     .content("ccc")
//                     .board(board)
//                     .user(user)
//                     .parentComment(null).build();
//             ReflectionTestUtils.setField(comment3, "id", 3L);
//             ReflectionTestUtils.setField(comment1, "createdAt", LocalDateTime.now());
//             ReflectionTestUtils.setField(comment1, "updatedAt", null);
//             ReflectionTestUtils.setField(comment1, "deletedAt", null);
//
//             commentList.add(comment1);
//             commentList.add(comment3);
//
//             return commentList;
//         }
//     }
//
// }
