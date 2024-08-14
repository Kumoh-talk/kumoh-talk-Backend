package com.example.demo.domain.board.service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.board.Repository.CategoryRepository;
import com.example.demo.domain.board.domain.entity.Category;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryQueryService {
	private final CategoryRepository categoryRepository;

	public List<String> getCategories() {
		return categoryRepository.findAll().stream()
			.map(category -> category.getName())
			.collect(Collectors.toList());
	}
}
