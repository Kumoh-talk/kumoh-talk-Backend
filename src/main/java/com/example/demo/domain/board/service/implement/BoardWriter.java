package com.example.demo.domain.board.service.implement;

import com.example.demo.domain.board.service.entity.BoardCore;
import com.example.demo.domain.board.service.entity.BoardInfo;
import com.example.demo.domain.board.service.repository.BoardRepository;
import com.example.demo.domain.user.domain.UserTarget;
import com.example.demo.infra.board.entity.Board;
import com.example.demo.infra.board.entity.BoardCategory;
import com.example.demo.infra.board.entity.Category;
import com.example.demo.application.board.dto.request.BoardUpdateRequest;
import com.example.demo.application.board.dto.response.BoardInfoResponse;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.infra.board.Repository.BoardCategoryJpaRepository;
import com.example.demo.infra.board.Repository.BoardJpaRepository;
import com.example.demo.infra.board.Repository.CategoryJpaRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BoardWriter {
    private final BoardJpaRepository boardJpaRepository;
    private final CategoryJpaRepository categoryJpaRepository;
    private final BoardCategoryJpaRepository boardCategoryJpaRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public BoardInfo createDraftBoard(UserTarget userTarget, BoardCore draftBoardCore) {
        return boardRepository.saveBoard(userTarget, draftBoardCore);
    }

    private void saveCategoryAndBoardCategory(Board board, String categoryName) {
        Category category = categoryJpaRepository.findByName(categoryName)
                .orElseGet(() -> categoryJpaRepository.save(new Category(categoryName)));
        BoardCategory boardCategory = new BoardCategory(board, category);
        boardCategoryJpaRepository.save(boardCategory);
    }

    @Transactional
    public BoardInfoResponse updateBoard(BoardUpdateRequest boardUpdateRequest,Board board)  {
        if(boardUpdateRequest.getIsPublished()) board.publishBoard(); // 게시 상태로 변경
        board.changeBoardInfo(boardUpdateRequest);
        board.changeHeadImageUrl(boardUpdateRequest.getBoardHeadImageUrl());

        changeBoardCategories(board, boardUpdateRequest.getCategoryName());

        List<String> categoryNames = board.getBoardCategories().stream()
                .map(boardCategory -> boardCategory.getCategory().getName())
                .collect(Collectors.toList());


        return BoardInfoResponse.from(board,
                board.getUser().getNickname(),
                boardJpaRepository.countLikesByBoardId(boardUpdateRequest.getId()),
                categoryNames);
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
                    Category category = categoryJpaRepository.findByName(categoryName).get();
                    BoardCategory boardCategory = boardCategoryJpaRepository.findByNameAndBoardId(categoryName,board.getId()).get();
                    if (boardCategoryJpaRepository.countBoardCategoryByCategoryId(category.getId()) == 1) {
                        board.getBoardCategories().remove(boardCategory);
                        categoryJpaRepository.delete(category);
                    }
                    board.getBoardCategories().remove(boardCategory);
                    category.getBoardCategories().remove(boardCategory);
                    boardCategoryJpaRepository.delete(boardCategory);
                });

        // 추가할 카테고리 처리
        newCategoryNames.stream()
                .filter(categoryName -> !existingCategoryNames.contains(categoryName))
                .forEach(categoryName -> {
                    saveCategoryAndBoardCategory(board, categoryName);
                });
    }

    @Transactional
    public void removeBoard(Board board) {
        List<BoardCategory> existingBoardCategories = board.getBoardCategories();
        List<String> existingCategoryNames = existingBoardCategories.stream()
            .map(boardCategory -> boardCategory.getCategory().getName())
            .toList();

        // 삭제할 카테고리 처리
        existingCategoryNames.stream()
            .forEach(categoryName -> {
                Category category = categoryJpaRepository.findByName(categoryName).get();
                BoardCategory boardCategory = boardCategoryJpaRepository.findByNameAndBoardId(categoryName,board.getId()).get();
                if (boardCategoryJpaRepository.countBoardCategoryByCategoryId(category.getId()) == 1) {
                    board.getBoardCategories().remove(boardCategory);
                    categoryJpaRepository.delete(category);
                }
                board.getBoardCategories().remove(boardCategory);
                category.getBoardCategories().remove(boardCategory);
                boardCategoryJpaRepository.delete(boardCategory);
            });
        boardJpaRepository.delete(board);
    }

    public Board validateBoardForDelete(Long userId, Long boardId) {
        Board board = validateBoard(boardId);
        validateUserEqualBoardUser(userId, board);
        return board;
    }

    private Board validateBoard(Long boardId) {
		return boardJpaRepository.findById(boardId)
                .orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));
    }

    private void validateUserEqualBoardUser(Long userId, Board board) {
        if(!board.getUser().getId().equals(userId)) {
            throw new ServiceException(ErrorCode.NOT_ACCESS_USER);
        }
    }

}
