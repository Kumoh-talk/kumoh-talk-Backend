package com.example.demo.domain.comment.repository;


import com.example.demo.base.RepositoryTest;
import com.example.demo.domain.board.Repository.BoardRepository;
import com.example.demo.domain.board.domain.BoardStatus;
import com.example.demo.domain.board.domain.entity.Board;
import com.example.demo.domain.comment.domain.entity.Comment;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.vo.Status;
import com.example.demo.domain.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.domain.user.domain.vo.Role.ROLE_USER;
import static org.assertj.core.api.Assertions.assertThat;

public class CommentRepositoryTest extends RepositoryTest {
    @Autowired
    private TestEntityManager tem;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private BoardRepository boardRepository;

    private Long boardId;
    private List<User> userList;
    private List<Board> boardList;
    private List<Comment> commentList;
    @BeforeEach
    void setup(){
        userList = TestDataFactory.createUsers();
        for (User user : userList)
            tem.persist(user);

        boardList = TestDataFactory.createBoards(userList);
        for (Board board : boardList)
            tem.persist(board);

        commentList = TestDataFactory.createComments(boardList, userList);
        for (Comment comment : commentList)
            tem.persist(comment);

        commentList.get(0).setParentComment(commentList.get(0));
        commentList.get(1).setParentComment(commentList.get(0));
        commentList.get(2).setParentComment(commentList.get(2));
        commentList.get(3).setParentComment(commentList.get(0));
    }
    @AfterEach
    void clear(){
        //userRepository.deleteAll();
        commentRepository.deleteAll();
        boardRepository.deleteAll();
    }

    @Nested
    @DisplayName("게시물 댓글 조회 리포지토리")
    class GetComments{
        @Test
        @DisplayName("게시물 조회 성공")
        public void success(){
            // given
            boardId = boardList.get(0).getId();
            // when
            List<Comment> comments= commentRepository.findByBoard_IdOrderByParentComment_IdAsc(boardId);
            // then
            Comment comment = comments.get(0);
            Comment insertComment = commentList.get(0);

            assertThat(comments.size()).isEqualTo(3);

            assertThat(comment.getContent()).isEqualTo(insertComment.getContent());
            assertThat(comment.getBoard().getId()).isEqualTo(insertComment.getBoard().getId());
            assertThat(comment.getUser().getId()).isEqualTo(insertComment.getUser().getId());
            assertThat(comment.getParentComment().getId()).isEqualTo(insertComment.getParentComment().getId());
            assertThat(comment.getDepth()).isEqualTo(insertComment.getDepth());
            assertThat(comment.getCreatedAt()).isEqualTo(insertComment.getCreatedAt());
            assertThat(comment.getUpdatedAt()).isEqualTo(insertComment.getUpdatedAt());
            assertThat(comment.getDeletedAt()).isEqualTo(insertComment.getDeletedAt());

            assertThat(comments.get(1).getParentComment().getId()).isLessThan(comments.get(2).getParentComment().getId());

        }
    }

    static class TestDataFactory{
        public static List<Board> createBoards(List<User> userList){
            List<Board> boardList = new ArrayList<>();
            Board board1 = Board.builder()
                    .title("제목1")
                    .content("내용1")
                    .status(BoardStatus.REAL)
                    .user(userList.get(0)).build();
            Board board2 = Board.builder()
                    .title("제목2")
                    .content("내용2")
                    .status(BoardStatus.REAL)
                    .user(userList.get(1)).build();

            boardList.add(board1);
            boardList.add(board2);

            return boardList;
        }
        public static List<User> createUsers(){
            List<User> userList = new ArrayList<>();
            User user1 = User.builder()
                    .name("a")
                    .nickname("aNick")
                    .email("a@")
                    .password("aPassword")
                    .role(ROLE_USER)
                    .department("컴퓨터공학과")
                    .status(Status.ATTENDING)
                    .field("프론트엔드").build();
            User user2 = User.builder()
                    .name("b")
                    .nickname("bNick")
                    .email("b@")
                    .password("bPassword")
                    .role(ROLE_USER)
                    .department("컴퓨터공학과")
                    .status(Status.ATTENDING)
                    .field("백엔드").build();
            User user3 = User.builder()
                    .name("c")
                    .nickname("cNick")
                    .email("c@")
                    .password("cPassword")
                    .role(ROLE_USER)
                    .department("컴퓨터공학과")
                    .status(Status.DEFERRAL)
                    .field("디자이너").build();

            userList.add(user1);
            userList.add(user2);
            userList.add(user3);

            return userList;
        }
        public static List<Comment> createComments(List<Board> board, List<User> userList){
            List<Comment> commentList = new ArrayList<>();
            Comment comment1 = Comment.builder()
                    .content("aaa")
                    .board(board.get(0))
                    .user(userList.get(0))
                    .depth(0).build();
            Comment comment2 = Comment.builder()
                    .content("bbb")
                    .board(board.get(1))
                    .user(userList.get(0))
                    .depth(0).build();
            Comment comment3 = Comment.builder()
                    .content("ccc")
                    .board(board.get(0))
                    .user(userList.get(1))
                    .depth(0).build();
            Comment comment4 = Comment.builder()
                    .content("ddd")
                    .board(board.get(0))
                    .user(userList.get(2))
                    .depth(1).build();

            commentList.add(comment1);
            commentList.add(comment2);
            commentList.add(comment3);
            commentList.add(comment4);

            return commentList;
        }
    }
}
