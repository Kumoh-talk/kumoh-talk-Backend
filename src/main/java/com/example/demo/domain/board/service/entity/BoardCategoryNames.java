package com.example.demo.domain.board.service.entity;

import java.util.List;

import lombok.Getter;

@Getter
public class BoardCategoryNames {
	private final List<String> categories;

	public BoardCategoryNames(List<String> categories) {
		this.categories = categories;
	}
}
