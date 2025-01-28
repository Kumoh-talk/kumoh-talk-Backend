package com.example.demo.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.domain.board.Repository.BoardRepository;
import com.example.demo.domain.user.repository.UserRepository;

@Component
public class BuilderSupporter {

	@Autowired
	private BoardRepository boardRepository;

	@Autowired
	private UserRepository userRepository;

	public BoardRepository boardRepository() {
		return boardRepository;
	}

	public UserRepository userRepository() {
		return userRepository;
	}
}
