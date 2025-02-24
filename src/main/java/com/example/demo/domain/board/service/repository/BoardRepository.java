package com.example.demo.domain.board.service.repository;

import com.example.demo.domain.board.service.entity.BoardCore;
import com.example.demo.domain.board.service.entity.BoardInfo;
import com.example.demo.domain.user.domain.UserTarget;

public interface BoardRepository {
	BoardInfo saveBoard(UserTarget userTarget, BoardCore boardCore);
}
