// package com.example.demo.domain.board.service;
//
// import com.example.demo.domain.board.Repository.BoardRepository;
// import com.example.demo.domain.board.Repository.ViewRepository;
// import com.example.demo.domain.board.domain.entity.Board;
// import com.example.demo.domain.board.domain.request.BoardRequest;
// import com.example.demo.domain.board.domain.response.BoardInfoResponse;
// import com.example.demo.domain.board.domain.entity.BoardCategory;
// import com.example.demo.domain.board.domain.entity.Category;
// import com.example.demo.domain.board.Repository.BoardCategoryRepository;
// import com.example.demo.domain.board.Repository.CategoryRepository;
// import com.example.demo.domain.user.domain.User;
// import com.example.demo.domain.user.domain.vo.Role;
// import com.example.demo.domain.user.repository.UserRepository;
// import com.example.demo.global.base.exception.ServiceException;
// import org.junit.jupiter.api.*;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
//
// import java.io.IOException;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Optional;
//
// import static org.assertj.core.api.Assertions.*;
// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.when;
//
// @ExtendWith(MockitoExtension.class)
// class BoardServiceTest {
//
//     @Mock
//     BoardCategoryRepository boardCategoryRepository;
//     @Mock
//     BoardRepository boardRepository;
//     @Mock
//     UserRepository userRepository;
//     @Mock
//     CategoryRepository categoryRepository;
//     @Mock
//     ViewRepository viewRepository;
//     @InjectMocks
//     BoardService boardService;
//     private User user;
//
//     @BeforeEach
//     void setUp() {
//         user = User.builder()
//                 .id(1L)
//                 .email("20200013@kumoh.ac.kr")
//                 .name("name")
//                 .nickname("nickname")
//                 .password("password")
//                 .role(Role.USER)
//                 .department("department")
//                 .status(Status.ATTENDING)
//                 .field("field")
//                 .build();
//
//     }
//     @AfterEach
//     public void clear() {
//         user = null;
//     }
//
//
//     @Nested
//     @DisplayName("게시물 저장")
//     class SaveTests {
//         @Test
//         @DisplayName("게시물 저장 성공")
//         void success() throws IOException {
//             // Given
//             List<String> names = new ArrayList<>();
//             names.add("category1");
//             names.add("category2");
//
//             BoardRequest boardRequest = BoardRequest.builder()
//                     .title("title")
//                     .contents("content")
//                     .categoryName(names)
//                     .build();
//
//             Board dummyBoard = BoardRequest.toEntity(boardRequest);
//             dummyBoard.setId(1L);
//
//             when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
//             names.forEach(name -> {
//                 Category dummyCategory = new Category(name);
//                 when(categoryRepository.findByName(name)).thenReturn(Optional.of(dummyCategory).stream().toList());
//             });
//
//             when(boardRepository.save(any(Board.class))).thenReturn(dummyBoard);
//
//
//
//
//             // When
//             BoardInfoResponse response = boardService.save(boardRequest, 0L);
//
//             // Then
//             assertThat(response.getBoardId()).isEqualTo(1L);
//             assertThat(response.getUsername()).isEqualTo(user.getName());
//             assertThat(response.getTitle()).isEqualTo(boardRequest.getTitle());
//             assertThat(response.getContents()).isEqualTo(boardRequest.getContents());
//
//
//         }
//
//         @Test
//         @DisplayName("게시물 저장 실패 : 유저 id 없는 요청")
//         void user_fail() throws IOException {
//             // Given
//             List<String> names = new ArrayList<>();
//             names.add("category1");
//             names.add("category2");
//
//             BoardRequest boardRequest = BoardRequest.builder()
//                     .title("title")
//                     .contents("content")
//                     .categoryName(names)
//                     .build();
//
//             when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());
//
//             //When-> Then
//             assertThrows(IllegalArgumentException.class , ()->{
//                 boardService.save(boardRequest, 0L);
//             });
//
//
//         }
//     }
//
//
//     @Nested
//     @DisplayName("게시물 조회")
//     class find {
//         @BeforeEach
//         void beforEach() {
//             // 조회 전용 기본 세팅
//             Board board = Board.builder()
//                     .id(1L)
//                     .title("title")
//                     .content("contentTest")
//                     .user(user)
//                     .build();
//         }
//         @Test
//         @DisplayName("게시물 조회 성공")
//         void find_success() {
//             //given
//             Board board = Board.builder()
//                     .id(1L)
//                     .title("title")
//                     .content("contentTest")
//                     .user(user)
//                     .build();
//             Long viewNum = 0L;
//             Long likeNum = 1L;
//             board.getBoardCategories().add(new BoardCategory("name1"));
//             board.getBoardCategories().add(new BoardCategory("name2"));
//
//
//             when(boardRepository.findById(any(Long.class))).thenReturn(Optional.of(board));
//             when(boardRepository.countViewsByBoardId(any(Long.class))).thenReturn(viewNum);
//             when(boardRepository.countLikesByBoardId(any(Long.class))).thenReturn(likeNum);
//             when(viewRepository.save(any())).thenReturn(null);
//
//
//             //when -> then
//             BoardInfoResponse byId = boardService.findById(board.getId());
//
//             assertThat(byId.getBoardId()).isEqualTo(board.getId());
//             assertThat(byId.getUsername()).isEqualTo(board.getUser().getName());
//             assertThat(byId.getTitle()).isEqualTo(board.getTitle());
//             assertThat(byId.getView()).isEqualTo(viewNum+1);
//             assertThat(byId.getCategoryNames().get(0)).isEqualTo("name1");
//             assertThat(byId.getCategoryNames().get(1)).isEqualTo("name2");
//         }
//         @Test
//         @DisplayName("게시물 조회 실패 : 유효하지 않은 boardId")
//         void find_fail() {
//             //given
//
//             when(boardRepository.findById(any(Long.class))).thenReturn(Optional.empty());
//
//             //when -> then
//             assertThrows(ServiceException.class, ()->{boardService.findById(1L);});
//
//         }
//
//     }
//
//     @Nested
//     @DisplayName("게시물 수정")
//     class update {
//         @Test
//         @DisplayName("게시물 수정 성공")
//         void success() throws IOException {
//             // Given
//             List<String> names = new ArrayList<>();
//             names.add("category1");
//             names.add("category2");
//
//             BoardRequest boardRequest = BoardRequest.builder()
//                     .title("title")
//                     .contents("content")
//                     .categoryName(names)
//                     .build();
//
//
//             Board board = Board.builder()
//                     .id(1L)
//                     .title("saveTitle")
//                     .content("savedContent")
//                     .user(user)
//                     .build();
//             board.getBoardCategories().add(new BoardCategory("name1"));
//             board.getBoardCategories().add(new BoardCategory("name2"));
//
//             Long viewNum = 0L;
//             Long likeNum = 1L;
//             when(boardRepository.findById(any(Long.class))).thenReturn(Optional.of(board));
//             when(boardRepository.countViewsByBoardId(any(Long.class))).thenReturn(viewNum);
//             when(boardRepository.countLikesByBoardId(any(Long.class))).thenReturn(likeNum);
//
//             board.getBoardCategories().clear();
//             Category dummyCategory = new Category(names.get(0));
//             when(categoryRepository.findByName(names.get(0))).thenReturn(Optional.of(dummyCategory).stream().toList());
//             Category dummyCategory1 = new Category(names.get(1));
//             when(categoryRepository.findByName(names.get(1))).thenReturn(Optional.of(dummyCategory1).stream().toList());
//
//             //when
//             BoardInfoResponse update = boardService.update(boardRequest, user.getId(), board.getId());
//
//             //then
//             assertThat(update.getTitle()).isEqualTo(boardRequest.getTitle());
//             assertThat(update.getContents()).isEqualTo(boardRequest.getContents());
//             assertThat(update.getCategoryNames().get(0)).isEqualTo(boardRequest.getCategoryName().get(0));
//             assertThat(update.getCategoryNames().get(1)).isEqualTo(boardRequest.getCategoryName().get(1));
//
//
//         }
//
//         @Test
//         @DisplayName("게시판 수정 실패 : 게시물 조회 실패")
//         void boardId_not_found() {
//             //given
//             BoardRequest boardRequest = BoardRequest.builder()
//                     .title("title")
//                     .contents("content")
//                     .build();
//             when(boardRepository.findById(any(Long.class))).thenReturn(Optional.empty());
//
//             //when -> then
//             assertThrows(ServiceException.class, ()->{boardService.update(boardRequest,0L,0L);});
//         }
//
//         @Test
//         @DisplayName("게시판 수정 실패 : 요청자의 유저와 게시판의 등록 유저가 다름")
//         void Not_Access() {
//             //given
//             List<String> names = new ArrayList<>();
//             names.add("category1");
//             names.add("category2");
//
//             BoardRequest boardRequest = BoardRequest.builder()
//                     .title("title")
//                     .contents("content")
//                     .categoryName(names)
//                     .build();
//             Board board = Board.builder()
//                     .id(1L)
//                     .title("saveTitle")
//                     .content("savedContent")
//                     .user(user)
//                     .build();
//             when(boardRepository.findById(any(Long.class))).thenReturn(Optional.of(board));
//
//             //when -> then
//             assertThrows(ServiceException.class, ()->{boardService.update(boardRequest,10L,board.getId());});
//
//         }
//     }
// }