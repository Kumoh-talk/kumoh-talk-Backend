package com.example.demo.domain.board.service;

import com.example.demo.domain.board.Repository.*;
import com.example.demo.domain.board.domain.entity.Board;
import com.example.demo.domain.board.domain.entity.View;
import com.example.demo.domain.board.domain.entity.BoardCategory;
import com.example.demo.domain.board.domain.entity.Category;
import com.example.demo.domain.board.domain.request.BoardCreateRequest;
import com.example.demo.domain.board.domain.request.BoardUpdateRequest;
import com.example.demo.domain.board.domain.response.BoardInfoResponse;
import com.example.demo.domain.board.domain.vo.Status;
import com.example.demo.domain.file.repository.FileRepository;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {


    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final BoardCategoryRepository boardCategoryRepository;
    @Transactional
    public BoardInfoResponse boardCreate(Long userId, BoardCreateRequest boardCreateRequest) {
        User user = validateUser(userId);

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
    public BoardInfoResponse updateBoard(BoardUpdateRequest boardUpdateRequest, Long userId) throws IOException {
        Board board = validateBoard(boardUpdateRequest.getId());
        validateUserEqualBoardUser(userId, board);

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
        Board board = validateBoard(boardId);
        validateUserEqualBoardUser(userId, board);

        List<Category> categories = board.getBoardCategories().stream()
                .map(BoardCategory::getCategory)
                .collect(Collectors.toList());


        boardRepository.deleteBoardCategoryByBoardId(boardId);
        boardRepository.deleteViewByBoardId(boardId);
        boardRepository.deleteFileByBoardId(boardId);
        boardRepository.deleteLikeByBoardId(boardId);

        boardRepository.delete(board);
        for (Category category : categories) {
            if (boardCategoryRepository.countBoardCategoryByCategoryId(category.getId()) == 0) {
                categoryRepository.delete(category);
            }
        }
    }

    private Board validateBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));
        return board;
    }

    private static void validateUserEqualBoardUser(Long userId, Board board) {
        if(!board.getUser().getId().equals(userId)) {
            throw new ServiceException(ErrorCode.NOT_ACCESS_USER);
        }
    }

    private User validateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
        return user;
    }

}
