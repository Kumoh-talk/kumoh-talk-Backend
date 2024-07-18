package com.example.demo.domain.board.service.component;

import com.example.demo.domain.board.Repository.BoardRepository;
import com.example.demo.domain.board.Repository.ViewRepository;


public interface ViewIncreaseCommand {
    void increaseView(Long boardId);
}
