// package com.example.demo.domain.board.Repository;
//
// import com.example.demo.base.RepositoryTest;
// import com.example.demo.infra.board.entity.Board;
// import com.example.demo.infra.board.entity.BoardCategory;
// import com.example.demo.infra.board.entity.Category;
// import com.example.demo.domain.board.domain.dto.vo.Status;
// import com.example.demo.domain.board.domain.dto.vo.Tag;
// import com.example.demo.domain.user.domain.User;
// import com.example.demo.domain.user.domain.vo.Role;
// import com.example.demo.domain.user.repository.UserRepository;
// import com.example.demo.global.oauth.user.OAuth2Provider;
// import jakarta.persistence.EntityManager;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//
// import java.util.List;
// import java.util.stream.Collectors;
//
// import static org.assertj.core.api.Assertions.assertThat;
//
// class BoardCategoryRepositoryTest extends RepositoryTest {
//     @Autowired
//     private BoardRepository boardRepository;
//     @Autowired
//     private UserRepository userRepository;
//     @Autowired
//     private CategoryRepository categoryRepository;
//     @Autowired
//     private TestEntityManager testEntityManager;
//     @Autowired
//     private BoardCategoryRepository boardCategoryRepository;
//     private EntityManager em;
//
//     @BeforeEach
//     void Init() {
//         em = testEntityManager.getEntityManager();
//     }
//
//     private User createUser() {
//         return User.builder()
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
//     @DisplayName("boardCategory boardId로 삭제 테스트")
//     void deleteByBoardIdTest() {
//         // given
//         // given
//         User user = createUser();
//         userRepository.save(user);
//
//         Board board = createBoard(user);
//
//         Category category = new Category("카테고리");
//         Category category1 = new Category("카테고리1");
//         categoryRepository.save(category);
//         categoryRepository.save(category1);
//
//         BoardCategory boardCategory = new BoardCategory(board, category);
//         BoardCategory boardCategory1 = new BoardCategory(board, category1);
//         boardRepository.save(board);
//         em.flush();
//         em.clear(); // 영속성 컨텍스트 초기화
//         board = boardRepository.findById(board.getId()).get();
//         assertThat(board.getBoardCategories().size()).isEqualTo(2);
//
//         //when
//         boardRepository.deleteBoardCategoryByBoardId(board.getId());
//         em.flush();
//         em.clear(); // 영속성 컨텍스트 초기화
//         //then
//         board = boardRepository.findById(board.getId()).get();
//         assertThat(board.getBoardCategories().size()).isEqualTo(0);
//     }
//
//     @Test
//     @DisplayName("BoardCategory 수 CategoryId로 조회 테스트")
//     void countByCategoryIdTest() {
//         // given
//         User user = createUser();
//         userRepository.save(user);
//
//         Board board = createBoard(user);
//
//         Category category = new Category("카테고리");
//         Category category1 = new Category("카테고리1");
//         categoryRepository.save(category);
//         categoryRepository.save(category1);
//
//         BoardCategory boardCategory = new BoardCategory(board, category);
//         BoardCategory boardCategory1 = new BoardCategory(board, category1);
//         boardRepository.save(board);
//         em.flush();
//         em.clear(); // 영속성 컨텍스트 초기화
//         board = boardRepository.findById(board.getId()).get();
//         assertThat(board.getBoardCategories().size()).isEqualTo(2);
//
//
//         // when
//         board = boardRepository.findById(board.getId()).get();
//         List<Category> categories = board.getBoardCategories().stream()
//                 .map(BoardCategory::getCategory)
//                 .collect(Collectors.toList());
//         boardRepository.deleteBoardCategoryByBoardId(board.getId());
//
//
//         em.flush();
//         em.clear(); // 영속성 컨텍스트 초기화
//         // then
//         board  = boardRepository.findById(board.getId()).get();
//         assertThat(board.getBoardCategories().size()).isEqualTo(0);
//         for (Category categorytmp : categories) {
//             assertThat(boardCategoryRepository.countBoardCategoryByCategoryId(categorytmp.getId())).isEqualTo(0);
//         }
//
//     }
// }