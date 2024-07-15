package com.example.demo.domain.board.service;

import com.example.demo.domain.board.Repository.ViewRepository;
import com.example.demo.domain.board.domain.entity.Board;
import com.example.demo.domain.board.domain.entity.View;
import com.example.demo.domain.board.domain.entity.BoardCategory;
import com.example.demo.domain.board.domain.entity.Category;
import com.example.demo.domain.board.Repository.BoardCategoryRepository;
import com.example.demo.domain.board.Repository.CategoryRepository;
import com.example.demo.domain.board.Repository.BoardRepository;
import com.example.demo.domain.board.domain.request.BoardCreateRequest;
import com.example.demo.domain.board.domain.request.BoardUpdateRequest;
import com.example.demo.domain.board.domain.response.BoardInfoResponse;
import com.example.demo.domain.board.domain.vo.Status;
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


    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ViewRepository viewRepository;
    private final BoardCategoryRepository boardCategoryRepository;
    private static final int PAGE_SIZE = 10;

    @Transactional
    public BoardInfoResponse boardCreate(Long userId, BoardCreateRequest boardCreateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));

        Board board = Board.fromBoardRequest(user,boardCreateRequest);
        board.changeBoardStatus(Status.DRAFT);
        clearAndAddCategoriesToBoard(board, boardCreateRequest.getCategoryName());

        Board savedBoard = boardRepository.save(board);

        return BoardInfoResponse.from(
                savedBoard,
                user.getNickname(),
                0L,
                0L,
                boardCreateRequest.getCategoryName());
    }

    @Transactional
    public BoardInfoResponse findByboardId(Long boardId) { // TODO : View 관련해서 의논해봐야함
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));
        String nickname = board.getUser().getNickname();
        Long viewNum = boardRepository.countViewsByBoardId(boardId);
        Long likeNum = boardRepository.countLikesByBoardId(boardId);

        View view = new View(board);
        viewRepository.save(view);

        List<String> categoryNames = boardRepository.findCategoryNameByBoardId(boardId);

        return BoardInfoResponse.from(
                board,
                nickname,
                viewNum+1,
                likeNum,
                categoryNames);
    }



    @Transactional
    public BoardInfoResponse updateBoard(BoardUpdateRequest boardUpdateRequest, Long userId) throws IOException {
        Board board = boardRepository.findById(boardUpdateRequest.getId())
                .orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));
        if(!board.getUser().getId().equals(userId)) {
            throw new ServiceException(ErrorCode.NOT_ACCESS_USER);
        }

        updateBoard(board, boardUpdateRequest);


        Long viewNum = boardRepository.countViewsByBoardId(boardUpdateRequest.getId());
        Long likeNum = boardRepository.countLikesByBoardId(boardUpdateRequest.getId());

        return BoardInfoResponse.from(board,
                board.getUser().getNickname(),
                viewNum,
                likeNum,
                boardUpdateRequest.getCategoryName());
    }

    private void updateBoard(Board board , BoardUpdateRequest boardUpdateRequest) {
        board.changeBoardInfo(boardUpdateRequest);
        board.changeBoardStatus(boardUpdateRequest.getStatus());
        clearAndAddCategoriesToBoard(board, boardUpdateRequest.getCategoryName());
    }

    private void clearAndAddCategoriesToBoard(Board board, List<String> categoryNames) {
        board.getBoardCategories().clear();
        categoryNames.forEach(categoryName -> {
            Category category = categoryRepository.findByName(categoryName)
                    .orElseGet(() -> categoryRepository.save(new Category(categoryName)));
            BoardCategory boardCategory = new BoardCategory(board, category);
            board.getBoardCategories().add(boardCategory);
            boardCategoryRepository.save(boardCategory);
        });

    }

    @Transactional
    public void removeBoard(Long userId,Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));
        if(!board.getUser().getId().equals(userId)) {
            new ServiceException(ErrorCode.NOT_ACCESS_USER);
        }
        //TODO : 연관된 엔티티들도 삭제 처리 할 지 고민중
        //TODO : Board 와 연관된 엔티티들도 soft delete 적용 고민중
        boardRepository.delete(board);
    }


//    @Transactional(readOnly = true)
//    public List<BoardInfoResponse> findByALL() {
///*        return postRepository.findAll().stream()
//                .map(post -> PostInfoResponse.from(post, post.getUser().getName()))
//                .collect(Collectors.toList());*/
//        return null;  // 추후 pagging 처리 추가
//    }

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
