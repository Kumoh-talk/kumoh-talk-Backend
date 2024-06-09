package com.example.demo.domain.board.service;

import com.example.demo.domain.board.Repository.ViewRepository;
import com.example.demo.domain.board.domain.entity.Board;
import com.example.demo.domain.board.domain.entity.View;
import com.example.demo.domain.category.domain.entity.BoardCategory;
import com.example.demo.domain.category.domain.entity.Category;
import com.example.demo.domain.category.repository.BoardCategoryRepository;
import com.example.demo.domain.category.repository.CategoryRepository;
import com.example.demo.domain.file.domain.entity.File;
import com.example.demo.domain.board.Repository.BoardRepository;
import com.example.demo.domain.board.domain.request.BoardRequest;
import com.example.demo.domain.board.domain.response.BoardInfoResponse;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    // TODO : 너무 의존성 많아지는데 Service 계층 분리 생각중..
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ViewRepository viewRepository;
    private final BoardCategoryRepository boardCategoryRepository;
    private static final int PAGE_SIZE = 10;
    /**
     *  파일 저장 메서드
     * @param boardRequest
     * @param userId
     * @return PostInfoResponse 생성된 post 정보 객체 반환
     */
    @Transactional
    public BoardInfoResponse save(BoardRequest boardRequest, Long userId) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다"));

        Board board = BoardRequest.toEntity(boardRequest);
        board.setUser(user);


        boardRequest.getCategoryName()
                .forEach(name ->{
            categoryRepository.findByName(name).stream().findAny()
                    .ifPresentOrElse(category -> {
                        board.getBoardCategories().add(new BoardCategory(board,category));
                    },()->{
                        board.getBoardCategories().add(new BoardCategory(board,new Category(name)));
                    });
        });

        Board savedBoard = boardRepository.save(board);
        return BoardInfoResponse.from(
                savedBoard,
                user.getNickname(),
                0L,
                0L,
                boardRequest.getCategoryName());
    }
    /**
     * 게시물 id로 게시물을 찾는 메서드
     * @param boardId
     * @return BoardInfoResponse
     */
    @Transactional
    public BoardInfoResponse findById(Long boardId) { // TODO : View 관련해서 의논해봐야함
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));
        String nickname = board.getUser().getNickname();
        Long viewNum = boardRepository.countViewsByBoardId(boardId);
        Long likeNum = boardRepository.countLikesByBoardId(boardId);

        View view = new View(board);
        viewRepository.save(view);

        List<String> categoryNames = new ArrayList<>();
        board.getBoardCategories().forEach(categoryName ->{categoryNames.add(categoryName.getCategoryName());});

        return BoardInfoResponse.from(
                board,
                nickname,
                viewNum+1,
                likeNum,
                categoryNames);
    }


    /**
     *
     * @param boardRequest
     * @param userId
     * @param boardId
     * @return BoardInfoResponse
     */
    @Transactional
    public BoardInfoResponse update(BoardRequest boardRequest, Long userId, Long boardId) throws IOException {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));
        if(!board.getUser().getId().equals(userId)) {
            throw new ServiceException(ErrorCode.NOT_ACCESS_USER);
        }
        board.setTitle(boardRequest.getTitle());
        board.setContent(boardRequest.getContents());

        board.getBoardCategories()
                        .forEach(boardCategory -> {
                            boardCategoryRepository.delete(boardCategory);
                        });

        boardRequest.getCategoryName()
                .forEach(name ->{
                    categoryRepository.findByName(name).stream().findAny()
                            .ifPresentOrElse(category -> {
                                board.getBoardCategories().add(new BoardCategory(board,category));
                            },()->{
                                board.getBoardCategories().add(new BoardCategory(board,new Category(name)));
                            });
                });
        Long viewNum = boardRepository.countViewsByBoardId(boardId);
        Long likeNum = boardRepository.countLikesByBoardId(boardId);
        return BoardInfoResponse.from(board,
                board.getUser().getNickname(),
                viewNum,
                likeNum,
                boardRequest.getCategoryName());
    }


    /**
     * 게시물 삭제 메서드
     * @param boardId
     */
//    @Transactional
//    public void remove(Long boardId,String userName) {
//        Board board = boardRepository.findById(boardId)
//                .orElseThrow(() -> new ServiceException(ErrorCode.POST_NOT_FOUND));
//        if(!board.getUser().getName().equals(userName)) {
//            new ServiceException(ErrorCode.NOT_ACCESS_USER);
//        }
//
//        fileS3Uploader.deleteAllFiles(board);
//        boardRepository.delete(board);
//    }


    /**
     * 전체 게시물을 불러오는 메서드
     * @return List<PostInfoResponse>
     */
    @Transactional(readOnly = true)
    public List<BoardInfoResponse> findByALL() {
/*        return postRepository.findAll().stream()
                .map(post -> PostInfoResponse.from(post, post.getUser().getName()))
                .collect(Collectors.toList());*/
        return null;  // 추후 pagging 처리 추가
    }

//    @Transactional(readOnly = true)
//    public BoardPageResponse findPageList(int page, Track track, PageSort pageSort) {
//        PageRequest pageRequest = (pageSort == PageSort.DESC) ? PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").descending()):
//                PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending());
//
//
//        Page<Board> postPage = boardRepository.findAllByTrack(track, pageRequest);
//
//        PageInfo pageInfo = new PageInfo(postPage.getSize(), postPage.getNumber(), postPage.getTotalPages(), pageSort);
//
//        List<PageTitleInfo> pageTitleInfoList = new ArrayList<>();
//        postPage.forEach(post -> {
//            pageTitleInfoList.add(PageTitleInfo.from(post, post.getUser().getName()));
//        });
//
//
//        return new BoardPageResponse(pageTitleInfoList, pageInfo);
//    }

//    public BoardInfoResponse updateBoard(BoardRequest boardRequest, Board board, String userName) throws IOException {
//
//        fileS3Uploader.deleteAllFiles(board);
//        board.setTitle(boardRequest.getTitle());
//        board.setContent(boardRequest.getContents());
//
//        return BoardInfoResponse.from(board, userName);
//    }
//


}
