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
    private final CategoryRepository categoryRepository;
    private final BoardCategoryRepository boardCategoryRepository;

    @Transactional
    public BoardInfoResponse createDraftBoard(User user, BoardCreateRequest boardCreateRequest) { //TODO : [Board] 대표이미지 미 설정시 첫 번째 이미지로 선정 및 그것 마저도 없으면 기본 이미지로 저장하게 하는데 프론트에서 처리할건지 물어봐야함
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

        updateBoardInfo(board, boardUpdateRequest);


        List<String> categoryNames = board.getBoardCategories().stream()
                .map(boardCategory -> boardCategory.getCategory().getName())
                .collect(Collectors.toList());


        return BoardInfoResponse.from(board,
                board.getUser().getNickname(),
                boardRepository.countViewsByBoardId(boardUpdateRequest.getId()),
                boardRepository.countLikesByBoardId(boardUpdateRequest.getId()),
                categoryNames);
    }

    private void updateBoardInfo(Board board , BoardUpdateRequest boardUpdateRequest) {
        board.changeBoardInfo(boardUpdateRequest);
        board.changeBoardStatus(boardUpdateRequest.getStatus());
        board.changeHeadImageUrl(boardUpdateRequest.getBoardHeadImageUrl());
        changeBoardCategories(board, boardUpdateRequest.getCategoryName());
    }

    private void changeBoardCategories(Board board, List<String> newCategoryNames) {
        List<BoardCategory> existingBoardCategories = board.getBoardCategories();
        List<String> existingCategoryNames = existingBoardCategories.stream()
                .map(boardCategory -> boardCategory.getCategory().getName())
                .toList();

        // 삭제할 카테고리 처리
        existingCategoryNames.stream()
                .filter(categoryName -> !newCategoryNames.contains(categoryName))
                .forEach(categoryName -> {
                    Category category = categoryRepository.findByName(categoryName).get();
                    BoardCategory boardCategory = boardCategoryRepository.findByName(categoryName).get();
                    if (boardCategoryRepository.countBoardCategoryByCategoryId(category.getId()) == 1) {
                        board.getBoardCategories().remove(boardCategory);
                        categoryRepository.delete(category);
                    }
                });

        // 추가할 카테고리 처리
        newCategoryNames.stream()
                .filter(categoryName -> !existingCategoryNames.contains(categoryName))
                .forEach(categoryName -> {
                    saveCategoryAndBoardCategory(board, categoryName);
                });
    }

    @Transactional
    public void removeBoard(Long userId,Long boardId) {
        Board board = validateBoard(boardId);
        validateUserEqualBoardUser(userId, board);

        List<BoardCategory> existingBoardCategories = board.getBoardCategories();
        List<String> existingCategoryNames = existingBoardCategories.stream()
            .map(boardCategory -> boardCategory.getCategory().getName())
            .toList();

        // 삭제할 카테고리 처리
        existingCategoryNames.stream()
            .forEach(categoryName -> {
                Category category = categoryRepository.findByName(categoryName).get();
                BoardCategory boardCategory = boardCategoryRepository.findByName(categoryName).get();
                if (boardCategoryRepository.countBoardCategoryByCategoryId(category.getId()) == 1) {
                    board.getBoardCategories().remove(boardCategory);
                    categoryRepository.delete(category);
                }
            });
        boardRepository.delete(board);
    }

    private Board validateBoard(Long boardId) {
		return boardRepository.findById(boardId)
                .orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));
    }

    private void validateUserEqualBoardUser(Long userId, Board board) {
        if(!board.getUser().getId().equals(userId)) {
            throw new ServiceException(ErrorCode.NOT_ACCESS_USER);
        }
    }

}
