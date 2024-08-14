package com.example.demo.domain.board.controller;

import static com.example.demo.global.base.dto.ResponseUtil.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.board.service.usecase.LikeUseCase;
import com.example.demo.global.aop.AssignUserId;
import com.example.demo.global.base.dto.ResponseBody;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LikeController {
	private final LikeUseCase likeUseCase;

	@AssignUserId
	@PreAuthorize("hasRole('ROLE_USER') and isAuthenticated()")
	@PostMapping("/v1/boards/{boardId}/like")
	public ResponseEntity<ResponseBody<Void>> like(Long userId,@PathVariable Long boardId) {
		likeUseCase.likeBoard(userId,boardId);
		return ResponseEntity.ok(createSuccessResponse());
	}
}
