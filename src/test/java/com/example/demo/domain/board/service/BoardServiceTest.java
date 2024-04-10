//package com.example.demo.domain.board.service;
//
//import com.example.demo.domain.board.Repository.BoardRepository;
//import com.example.demo.domain.board.domain.request.BoardRequest;
//import com.example.demo.domain.category.repository.CategoryRepository;
//import com.example.demo.domain.user.domain.User;
//import com.example.demo.domain.user.domain.vo.Role;
//import com.example.demo.domain.user.domain.vo.Status;
//import com.example.demo.domain.user.repository.UserRepository;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.Spy;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import javax.naming.Name;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class BoardServiceTest {
//
//    @Mock
//    BoardRepository boardRepository;
//    @Spy
//    UserRepository userRepository;
//    @Mock
//    CategoryRepository categoryRepository;
//    private User user;
//
//    @BeforeEach
//    void setUp() {
//        user = User.builder()
//                .email("20200013@kumoh.ac.kr")
//                .name("name")
//                .nickname("nickname")
//                .password("password")
//                .role(Role.USER)
//                .department("department")
//                .status(Status.ATTENDING)
//                .field("field")
//                .build();
//
//    }
//    @AfterEach
//    public void clear() {
//        user = null;
//    }
//
//    @Nested
//    @DisplayName("게시물 저장")
//    class save{
//        @Test
//        @DisplayName("게시물 성공적 저장")
//        void success() {
//            //given
//            List<String> Names = new ArrayList<>();
//            Names.add("name1");
//            Names.add("name2");
//            BoardRequest boardRequest = BoardRequest.builder()
//                    .title("title")
//                    .contents("content")
//                    .categoryName(Names)
//                    .build();
//            when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));
//            when(categoryRepository.findByName(any(String.class)).stream().findAny()).thenReturn(Optional.ofNullable());
//
//
//        }
//    }
//}