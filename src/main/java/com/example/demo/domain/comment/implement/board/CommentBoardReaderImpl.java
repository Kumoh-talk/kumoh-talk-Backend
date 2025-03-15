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
    public boolean existsById(Long id) {
        return boardRepository.findBoardInfo(id).isPresent();
    }

    @Override
    public boolean existsByIdWithUser(Long id) {
        return boardRepository.findBoardInfo(id).isPresent();
    }

    @Override
    public Long getUserIdById(Long id) {
        BoardInfo boardInfo = boardRepository.findBoardInfo(id).orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));
        return boardInfo.getUserTarget().getUserId();
    }


}
