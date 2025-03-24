package com.example.demo.infra.fixture.board;

import com.example.demo.infra.board.entity.Board;
import com.example.demo.infra.board.entity.Like;
import com.example.demo.infra.user.entity.User;

public class LikeFixtures {
    public static Like LIKE(User user, Board board) {
        return new Like(user, board);
    }
}
