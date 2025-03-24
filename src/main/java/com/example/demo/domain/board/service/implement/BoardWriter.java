package com.example.demo.domain.board.service.implement;

import com.example.demo.domain.user.entity.UserTarget;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.board.service.entity.BoardContent;
import com.example.demo.domain.board.service.entity.BoardInfo;
import com.example.demo.domain.board.service.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BoardWriter {
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

    public void removeBoardContent(BoardInfo savedBoardInfo) {
        boardRepository.deleteBoard(savedBoardInfo.getBoardId());
    }

    public void removeBoardByBoardId(Long boardId) {
        boardRepository.deleteBoard(boardId);
    }
}
