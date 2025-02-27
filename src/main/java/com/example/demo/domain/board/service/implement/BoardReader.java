package com.example.demo.domain.board.service.implement;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.base.page.GlobalPageableDto;
import com.example.demo.domain.board.service.entity.BoardInfo;
import com.example.demo.domain.board.service.entity.BoardTitleInfo;
import com.example.demo.domain.board.service.entity.DraftBoardTitle;
import com.example.demo.domain.board.service.entity.vo.BoardType;
import com.example.demo.domain.board.service.repository.BoardRepository;

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
    public GlobalPageableDto<BoardTitleInfo> findPublishedBoardPageList(BoardType boardType, GlobalPageableDto pageableDto) {
        pageableDto.setPage(boardRepository.findBoardTitleInfoPage(boardType, pageableDto));
        return pageableDto;
    }

    public GlobalPageableDto<DraftBoardTitle> findDraftBoardPageList(Long userId, GlobalPageableDto pageableDto) {
        pageableDto.setPage(boardRepository.findDraftBoardByPage(userId, pageableDto));
        return pageableDto;
    }

    @Transactional(readOnly = true)
    public GlobalPageableDto<BoardTitleInfo> findPublishedBoardListByUser(Long userId,
        BoardType boardType,
        GlobalPageableDto pageableDto) {
        pageableDto.setPage(boardRepository.findPublishedBoardListByUser(userId,boardType,pageableDto.getPageable()));
        return pageableDto;
    }

    public String readBoardAttachFileUrl(Long boardId) {
        return boardRepository.getBoardAttachFileUrl(boardId);
    }
}
