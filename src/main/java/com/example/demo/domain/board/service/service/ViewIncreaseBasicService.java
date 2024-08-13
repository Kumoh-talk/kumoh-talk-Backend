package com.example.demo.domain.board.service.service;

import com.example.demo.domain.board.Repository.BoardRepository;
import com.example.demo.domain.board.Repository.ViewRepository;
import com.example.demo.domain.board.domain.entity.Board;
import com.example.demo.domain.board.domain.entity.View;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
public class ViewIncreaseBasicService implements ViewIncreaseService {//TODO : [Board] 조회수 증가 로직 IP 로 구현하면 바꿀 예정
    private final BoardRepository boardRepository;
    private final ViewRepository viewRepository;

    @Transactional
    @Override
    public void increaseView(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));
        View view = new View(board);
        viewRepository.save(view);
    }
}
