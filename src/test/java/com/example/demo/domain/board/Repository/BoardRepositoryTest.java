// package com.example.demo.domain.board.Repository;
//
// import com.example.demo.base.RepositoryTest;
// import com.example.demo.domain.board.domain.entity.*;
// import com.example.demo.domain.board.domain.dto.vo.Status;
// import com.example.demo.domain.board.domain.dto.vo.Tag;
// import com.example.demo.domain.user.domain.User;
// import com.example.demo.domain.user.domain.vo.Role;
// import com.example.demo.domain.user.repository.UserRepository;
// import com.example.demo.global.oauth.user.OAuth2Provider;
// import jakarta.persistence.EntityManager;
//
// import org.junit.jupiter.api.*;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//
// import static org.assertj.core.api.Assertions.*;
//
// class BoardRepositoryTest extends RepositoryTest{
//
//
//
//     @Autowired
//     private BoardRepository boardRepository;
//     @Autowired
//     private UserRepository userRepository;
//     @Autowired
//     private CategoryRepository categoryRepository;
//     @Autowired
//     private ViewRepository viewRepository;
//
//     @Autowired
//     private TestEntityManager testEntityManager;
//
//     private EntityManager em;
//     @BeforeEach
//     void Init() {
//         em = testEntityManager.getEntityManager();
//     }
//
//
//     private User createUser() {
//          return User.builder()
//                 .provider(OAuth2Provider.GOOGLE)
//                 .providerId("123456789")
//                 .nickname("exampleNickname")
//                 .role(Role.ROLE_USER)
//                 .build();
//     }
//
//     private Board createBoard(User user) {
//         return Board.builder()
//                 .title("제목")
//                 .content("내용")
//                 .status(Status.DRAFT)
//                 .tag(Tag.notice)
//                 .user(user)
//                 .build();
//     }
//
//     @Test
//     @DisplayName("단독 Board 저장")
//     void 저장() {
//         //given
//         User user = createUser();
//         Board board = createBoard(user);
//         userRepository.save(user);
//
//         //when
//         Board save = boardRepository.save(board);
//         em.flush();
//
//         // then
//         Board savedBoard = boardRepository.findById(board.getId()).get();
//
//         assertThat(savedBoard.getId()).isEqualTo(save.getId());
//         assertThat(savedBoard.getTitle()).isEqualTo(save.getTitle());
//         assertThat(savedBoard.getContent()).isEqualTo(save.getContent());
//         assertThat(savedBoard.getStatus()).isEqualTo(save.getStatus());
//         assertThat(savedBoard.getTag()).isEqualTo(save.getTag());
//         assertThat(savedBoard.getUser().getId()).isEqualTo(save.getUser().getId());
//
//     }
//
//     @Test
//     @DisplayName("view 숫자 조회 쿼리 테스트")
//     void viewCount() {
//         //given
//         User user = createUser();
//         Board board = createBoard(user);
//         userRepository.save(user);
//
//         View view = new View(board);
//         View view1 = new View(board);
//
//         //when
//         Board save = boardRepository.save(board);
//         viewRepository.save(view);
//         viewRepository.save(view1);
//         em.flush();
//
//         //then
//         assertThat(boardRepository.countViewsByBoardId(save.getId())).isEqualTo(2L);
//     }
//
//     @Test
//     @DisplayName("like 숫자 조회 쿼리 테스트")
//     void likeCount() {
//         //given
//         User user = createUser();
//         Board board = createBoard(user);
//         userRepository.save(user);
//
//         Like like = new Like(user, board);
//         Like like1 = new Like(user, board);
//
//         //when
//         Board save = boardRepository.save(board);
//         em.flush();
//
//         //then
//         assertThat(boardRepository.countLikesByBoardId(save.getId())).isEqualTo(2L);
//     }
//
//     @Test
//     @DisplayName("게시물 id 로 카테고리 이름 list 조회")
//     void findCategoryNameByBoardId() {
//         //given
//         User user = createUser();
//         Board board = createBoard(user);
//         userRepository.save(user);
//
//         Category category = new Category("카테고리");
//         Category category1 = new Category("카테고리1");
//         categoryRepository.save(category);
//         categoryRepository.save(category1);
//
//         BoardCategory boardCategory = new BoardCategory(board, category);
//         BoardCategory boardCategory1 = new BoardCategory(board, category1);
//
//         //when
//         Board save = boardRepository.save(board);
//         em.flush();
//
//         //then
//         assertThat(boardRepository.findCategoryNameByBoardId(save.getId()).get(0)).isEqualTo("카테고리");
//         assertThat(boardRepository.findCategoryNameByBoardId(save.getId()).get(1)).isEqualTo("카테고리1");
//     }
// }