package com.example.demo.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.domain.board.domain.entity.Board;
import com.example.demo.domain.user.domain.User;

@Component
public class TestFixtureBuilder {
	@Autowired
	private BuilderSupporter bs;

	public Board buildBoard(Board board){
		return bs.boardRepository().save(board);
	}

	public User buildUser(User user){
		return bs.userRepository().save(user);
	}
}
