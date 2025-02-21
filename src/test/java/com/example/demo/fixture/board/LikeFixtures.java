package com.example.demo.fixture.board;

import com.example.demo.domain.board.domain.entity.Board;
import com.example.demo.domain.board.domain.entity.Like;
import com.example.demo.domain.user.domain.User;

public class LikeFixtures {
    public static Like LIKE(User user, Board board) {
        return new Like(user, board);
    }
}
