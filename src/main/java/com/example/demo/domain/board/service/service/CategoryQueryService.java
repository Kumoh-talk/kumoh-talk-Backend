package com.example.demo.domain.board.service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.infra.board.Repository.CategoryJpaRepository;
import com.example.demo.application.board.dto.response.BoardTitleInfoResponse;
import com.example.demo.global.base.dto.page.GlobalPageResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryQueryService {
	private final CategoryJpaRepository categoryJpaRepository;

	@Transactional(readOnly = true)
	public List<String> getCategories() {
		return categoryJpaRepository.findAll().stream()
			.map(category -> category.getName())
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public GlobalPageResponse<BoardTitleInfoResponse> getBoardPageByCategoryName(String categoryName, Pageable pageable) {
		return GlobalPageResponse.create(categoryJpaRepository.findBoardByPage(categoryName,pageable));
	}
}
