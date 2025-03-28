package com.example.demo.application.board.controller;

import static com.example.demo.global.base.dto.ResponseUtil.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.application.board.api.AdminBoardApi;
import com.example.demo.domain.board.service.service.BoardAdminService;
import com.example.demo.global.base.dto.ResponseBody;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/v1/admin/boards")
@RestController
@RequiredArgsConstructor
public class AdminBoardController implements AdminBoardApi {
	private final BoardAdminService boardAdminService;

	@PreAuthorize("hasRole('ROLE_ADMIN') and isAuthenticated()")
	@DeleteMapping("/{boardId}")
	public ResponseEntity<ResponseBody<Void>> deleteBoard(@PathVariable Long boardId) {
		return ResponseEntity.ok(createSuccessResponse(boardAdminService.deleteBoard(boardId)));
	}
}
