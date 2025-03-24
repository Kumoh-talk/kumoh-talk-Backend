package com.example.demo.domain.board.service.implement;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.board.service.entity.BoardInfo;
import com.example.demo.domain.board.service.entity.BoardTitleInfo;
import com.example.demo.domain.board.service.entity.DraftBoardTitle;
import com.example.demo.domain.board.service.entity.vo.BoardType;
import com.example.demo.domain.board.service.repository.BoardRepository;
import com.example.demo.global.base.dto.page.GlobalPageResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BoardReader {
    private final BoardRepository boardRepository;

    @Transactional(readOnly = true)
    public Optional<BoardInfo> searchSingleBoard(Long boardId) {
        return boardRepository.findBoardInfo(boardId);
    }

    @Transactional(readOnly = true)
    public GlobalPageResponse<BoardTitleInfo> findPublishedBoardPageList(BoardType boardType, Pageable pageable) {
        return GlobalPageResponse.create(boardRepository.findBoardTitleInfoPage(boardType, pageable));
    }

    public GlobalPageResponse<DraftBoardTitle> findDraftBoardPageList(Long userId, Pageable pageable) {
        return GlobalPageResponse.create(boardRepository.findDraftBoardByPage(userId, pageable));
    }

    @Transactional(readOnly = true)
    public GlobalPageResponse<BoardTitleInfo> findPublishedBoardListByUser(Long userId,
        BoardType boardType,
        Pageable pageable) {
        return GlobalPageResponse.create(boardRepository.findPublishedBoardListByUser(userId,boardType,pageable));
    }

    public String readBoardAttachFileUrl(Long boardId) {
        return boardRepository.getBoardAttachFileUrl(boardId);
    }
}
