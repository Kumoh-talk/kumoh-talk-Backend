package com.example.demo.domain.board.service.service;

import com.example.demo.domain.board.Repository.*;
import com.example.demo.domain.board.domain.entity.Board;
import com.example.demo.domain.board.domain.entity.BoardCategory;
import com.example.demo.domain.board.domain.entity.Category;
import com.example.demo.domain.board.domain.dto.request.BoardCreateRequest;
import com.example.demo.domain.board.domain.dto.request.BoardUpdateRequest;
import com.example.demo.domain.board.domain.dto.response.BoardInfoResponse;
import com.example.demo.domain.board.domain.dto.vo.Status;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BoardCommandService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final BoardCategoryRepository boardCategoryRepository;

    @Transactional
    public BoardInfoResponse createBoard(Long userId, BoardCreateRequest boardCreateRequest) {
        User user = validateUser(userId);

        Board board = Board.fromBoardRequest(user,boardCreateRequest);
        board.changeBoardStatus(Status.DRAFT);

        Board savedBoard = boardRepository.save(board);

        boardCreateRequest.getCategoryName().forEach(categoryName -> {
            saveCategoryAndBoardCategory(board, categoryName);
        });

        return BoardInfoResponse.from(
                savedBoard,
                user.getNickname(),
                0L,
                0L,
                boardCreateRequest.getCategoryName());
    }

    private void saveCategoryAndBoardCategory(Board board, String categoryName) {
        Category category = categoryRepository.findByName(categoryName)
                .orElseGet(() -> categoryRepository.save(new Category(categoryName)));
        BoardCategory boardCategory = new BoardCategory(board, category);
        boardCategoryRepository.save(boardCategory);
    }

    @Transactional
    public BoardInfoResponse updateBoard(BoardUpdateRequest boardUpdateRequest, Long userId)  {
        Board board = validateBoard(boardUpdateRequest.getId());
        validateUserEqualBoardUser(userId, board);

        updateBoard(board, boardUpdateRequest);


        Long viewNum = boardRepository.countViewsByBoardId(boardUpdateRequest.getId());
        Long likeNum = boardRepository.countLikesByBoardId(boardUpdateRequest.getId());

        List<String> categoryNames = board.getBoardCategories().stream()
                .map(boardCategory -> boardCategory.getCategory().getName())
                .collect(Collectors.toList());

        return BoardInfoResponse.from(board,
                board.getUser().getNickname(),
                viewNum,
                likeNum,
                categoryNames);
    }

    private void updateBoard(Board board , BoardUpdateRequest boardUpdateRequest) {
        board.changeBoardInfo(boardUpdateRequest);
        board.changeBoardStatus(boardUpdateRequest.getStatus());
        changeBoardCategories(board, boardUpdateRequest.getCategoryName());
    }

    private void changeBoardCategories(Board board, List<String> newCategoryNames) {
        List<BoardCategory> existingBoardCategories = board.getBoardCategories();
        List<String> existingCategoryNames = existingBoardCategories.stream()
                .map(boardCategory -> boardCategory.getCategory().getName())
                .collect(Collectors.toList());

        // 삭제할 카테고리 처리
        existingBoardCategories.stream()
                .filter(boardCategory -> !newCategoryNames.contains(boardCategory.getCategory().getName()))
                .forEach(boardCategory -> {
                    Category category = boardCategory.getCategory();
                    boardCategory.setAssociateNull();
                    category.getBoardCategories().remove(boardCategory);
                    board.getBoardCategories().remove(boardCategory);
                    boardCategoryRepository.delete(boardCategory);
                    deleteCategory(category);
                });

        // 추가할 카테고리 처리
        newCategoryNames.stream()
                .filter(categoryName -> !existingCategoryNames.contains(categoryName))
                .forEach(categoryName -> {
                    saveCategoryAndBoardCategory(board, categoryName);
                });
    }

    private void deleteCategory(Category category) {
        if (boardCategoryRepository.countBoardCategoryByCategoryId(category.getId()) == 0) {
            categoryRepository.delete(category);
        }
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
            deleteCategory(category);
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
