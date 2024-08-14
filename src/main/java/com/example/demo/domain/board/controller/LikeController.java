package com.example.demo.domain.board.controller;

import static com.example.demo.global.base.dto.ResponseUtil.*;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.board.domain.dto.response.BoardPageResponse;
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
	@PostMapping("/v1/boards/{boardId}/likes")
	public ResponseEntity<ResponseBody<Void>> saveLike(Long userId,@PathVariable Long boardId) {
		likeUseCase.likeBoard(userId,boardId);
		return ResponseEntity.ok(createSuccessResponse());
	}

	@AssignUserId
	@PreAuthorize("hasRole('ROLE_USER') and isAuthenticated()")
	@DeleteMapping("/v1/boards/{boardId}/likes")
	public ResponseEntity<ResponseBody<Void>> deleteLike(Long userId,@PathVariable Long boardId) {
		likeUseCase.unlikeBoard(userId,boardId);
		return ResponseEntity.ok(createSuccessResponse());
	}


	@AssignUserId
	@PreAuthorize("hasRole('ROLE_USER') and isAuthenticated()")
	@GetMapping("/v1/users/likes")
	public ResponseEntity<ResponseBody<BoardPageResponse>> getLikes(
		Long userId,
		@RequestParam(defaultValue = "1") int pageNum,
		@RequestParam(defaultValue = "10") int pageSize,
		@RequestParam(defaultValue = "DESC") String sortDirection,
		@RequestParam(defaultValue = "createdAt") String sortBy){
		Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by(Sort.Direction.fromString(sortDirection), sortBy));
		return ResponseEntity.ok(createSuccessResponse(likeUseCase.getLikes(userId,pageable)));
	}
}
