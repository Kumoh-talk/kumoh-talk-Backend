package com.example.demo.application.board.controller;

import static com.example.demo.global.base.dto.ResponseUtil.*;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.application.board.api.LikeApi;
import com.example.demo.domain.board.service.entity.BoardTitleInfo;
import com.example.demo.domain.board.service.service.LikeService;
import com.example.demo.global.aop.AssignUserId;
import com.example.demo.global.base.dto.ResponseBody;
import com.example.demo.global.base.dto.page.GlobalPageResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LikeController implements LikeApi {
	private final LikeService likeService;

	@AssignUserId
	@PreAuthorize("hasRole('ROLE_USER') and isAuthenticated()")
	@PostMapping("/v1/boards/{boardId}/likes")
	public ResponseEntity<ResponseBody<Void>> saveLike(Long userId,@PathVariable Long boardId) {
		likeService.likeBoard(userId,boardId);
		return ResponseEntity.ok(createSuccessResponse());
	}

	@AssignUserId
	@PreAuthorize("hasRole('ROLE_USER') and isAuthenticated()")
	@DeleteMapping("/v1/boards/{boardId}/likes")
	public ResponseEntity<ResponseBody<Void>> deleteLike(Long userId,@PathVariable Long boardId) {
		likeService.unlikeBoard(userId,boardId);
		return ResponseEntity.ok(createSuccessResponse());
	}


	@AssignUserId
	@PreAuthorize("hasRole('ROLE_USER') and isAuthenticated()")
	@GetMapping("/v1/users/likes")
	public ResponseEntity<ResponseBody<GlobalPageResponse<BoardTitleInfo>>> getLikes(
		Long userId,
		@PageableDefault(page=0, size=10,sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
		return ResponseEntity.ok(createSuccessResponse(
			likeService.getLikes(userId, pageable)));
	}
}
