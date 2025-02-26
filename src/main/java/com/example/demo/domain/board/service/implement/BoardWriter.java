package com.example.demo.domain.board.service.implement;

import com.example.demo.domain.board.service.entity.BoardContent;
import com.example.demo.domain.board.service.entity.BoardInfo;
import com.example.demo.domain.board.service.repository.BoardRepository;
import com.example.demo.domain.user.domain.UserTarget;
import com.example.demo.infra.board.entity.Board;
import com.example.demo.infra.board.category.entity.BoardCategory;
import com.example.demo.infra.board.category.entity.Category;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.infra.board.category.repository.BoardCategoryJpaRepository;
import com.example.demo.infra.board.Repository.BoardJpaRepository;
import com.example.demo.infra.board.category.repository.CategoryJpaRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BoardWriter {
    private final BoardJpaRepository boardJpaRepository;
    private final CategoryJpaRepository categoryJpaRepository;
    private final BoardCategoryJpaRepository boardCategoryJpaRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public BoardInfo createDraftBoard(UserTarget userTarget, BoardContent draftBoardCore) {
        return boardRepository.saveBoard(userTarget, draftBoardCore);
    }

    @Transactional
    public BoardInfo modifyBoarContent(BoardInfo savedBoardInfo, BoardContent updateBoardContent,Boolean isPublished) {
        Long boardId = savedBoardInfo.getBoardId();

        if(isPublished) updateBoardContent.publishBoard();
        else updateBoardContent.draftBoard();

        // 게시물 종류는 수정 불가능
        updateBoardContent.setBoardType(savedBoardInfo.getBoardContent().getBoardType());

        boardRepository.updateBoardContent(boardId, updateBoardContent);
        return savedBoardInfo.setBoardContent(updateBoardContent);
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

    public void removeBoardContent(BoardInfo savedBoardInfo) {
        boardRepository.deleteBoard(savedBoardInfo.getBoardId());
    }
}
