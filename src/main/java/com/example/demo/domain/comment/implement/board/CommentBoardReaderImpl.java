package com.example.demo.domain.comment.implement.board;

import com.example.demo.domain.board.service.entity.BoardInfo;
import com.example.demo.domain.board.service.repository.BoardRepository;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentBoardReaderImpl implements GenericCommentBoardReader {
    private final BoardRepository boardRepository;

    @Override
    public Long getById(Long id) {
        BoardInfo boardInfo = boardRepository.findBoardInfo(id)
                .orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));

        return boardInfo.getBoardId();
    }

    @Override
    public Long getByIdWithUser(Long id) {
        BoardInfo boardInfo = boardRepository.findBoardInfo(id)
                .orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));

        return boardInfo.getBoardId();
    }

}
